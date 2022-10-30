package ru.yofik.athena.common.storage.fine;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import ru.yofik.athena.common.storage.fine.entityDescriptor.EntityDescriptor;
import ru.yofik.athena.common.storage.fine.extractors.IdResultSetExtractor;
import ru.yofik.athena.common.storage.fine.extractors.MultipleEntityExtractor;
import ru.yofik.athena.common.storage.fine.extractors.NestedEntityIdsResultSetExtractor;
import ru.yofik.athena.common.storage.fine.extractors.SingleEntityExtractor;

import java.util.*;
import java.util.stream.Collectors;

/**
 * FineDao is an abstract class which implements base CRUD queries to SQL database:
 * <ul>
 *     <li>{@link FineDao#createQuery(Object, EntityDescriptor)} - single insert query creating self entity</li>
 *     <li>{@link FineDao#updateQuery(Object, EntityDescriptor)} - single update query updating all domain self columns of the entity</li>
 *     <li>{@link FineDao#deleteQuery(long, EntityDescriptor, boolean)} - single delete query deleting self entity or multiple delete queries to delete whole aggregate</li>
 *     <li>{@link FineDao#selectByIdQuery(long, EntityDescriptor, SingleEntityExtractor)} - single select query with joins returning whole aggregate</li>
 *     <li>{@link FineDao#selectPageQuery(Map, List, int, int, EntityDescriptor, SingleEntityExtractor)} - single select query with limit and offset to return list of entities. With filters</lit>
 *     <li>{@link FineDao#selectByQuery(Map, EntityDescriptor, SingleEntityExtractor)} - single select query with joins returning whole aggregate. With filters</li>
 * </ul>
 */
@AllArgsConstructor
@Slf4j
public abstract class FineDao {
    protected final NamedParameterJdbcTemplate template;

    public <T> T createQuery(
            T entity,
            EntityDescriptor<T> entityDescriptor,
            SingleEntityExtractor<T> entityExtractor
    ) {
        var newUserId = template.query(
                SqlPatterns.insert(entityDescriptor),
                NamedParams.selfDomainWithProvidedForeignKeysAsMap(
                        entity,
                        entityDescriptor,
                        provideForeignKeysMapCreateNestedEntitiesIfNeeded(entity, entityDescriptor),
                        false
                ),
                new IdResultSetExtractor()
        );

        log.debug("SUCCESS INSERT query for entity (tableName): " + entityDescriptor.tableName());

        if (newUserId == null) {
            log.error("SUCCESS INSERT query has returned null id!");
            throw new RuntimeException("Can't obtain entity id after creation");
        }

        return selectByIdQuery(newUserId, entityDescriptor, entityExtractor);
    }

    public <T> Long createQuery(
            T entity,
            EntityDescriptor<T> entityDescriptor
    ) {
        return template.query(
                SqlPatterns.insert(entityDescriptor),
                NamedParams.selfDomainWithProvidedForeignKeysAsMap(
                        entity,
                        entityDescriptor,
                        provideForeignKeysMapCreateNestedEntitiesIfNeeded(entity, entityDescriptor),
                        false
                ),
                new IdResultSetExtractor()
        );
    }

    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public <T> void updateQuery(T entity, EntityDescriptor<T> entityDescriptor) {
        entityDescriptor.nestedEntityDescriptors()
                        .forEach((foreignKeyColumn, nestedEntityDescriptor) -> {
                            var nestedEntity = entityDescriptor.extractValue(
                                    foreignKeyColumn,
                                    entity
                            );
                            updateQuery(nestedEntity, (EntityDescriptor) nestedEntityDescriptor);
                            log.debug("SUCCESS UPDATE query. Entity(tableName): " + nestedEntityDescriptor.tableName());
                        });

        template.update(
                SqlPatterns.update(entityDescriptor),
                NamedParams.selfAllAsMap(entity, entityDescriptor, false)
        );
        log.debug("SUCCESS UPDATE query. Entity(tableName): " + entityDescriptor.tableName());
    }

    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public void deleteQuery(long id, EntityDescriptor<?> entityDescriptor, boolean asAggregate) {
        if (asAggregate) {
            deleteAsAggregate(id, entityDescriptor);
        } else {
            template.update(
                    SqlPatterns.delete(entityDescriptor),
                    NamedParams.idAsMap(id, entityDescriptor, false)
            );
            log.debug("SUCCESS DELETE query. Entity(tableName): " + entityDescriptor.tableName());
        }
    }

    private void deleteAsAggregate(long id, EntityDescriptor<?> entityDescriptor) {
        var nestedEntityIds = template.query(
                SqlPatterns.selectForeignKeys(entityDescriptor),
                NamedParams.idAsMap(id, entityDescriptor, false),
                new NestedEntityIdsResultSetExtractor(entityDescriptor)
        );
        log.debug("SUCCESS SELECT BY ID query to retrieve values of foreign keys for associated delete queries. Entity(tableName): " + entityDescriptor.tableName());

        template.update(
                SqlPatterns.delete(entityDescriptor),
                NamedParams.idAsMap(id, entityDescriptor, false)
        );
        log.debug("SUCCESS DELETE query. Entity(tableName): " + entityDescriptor.tableName());

        entityDescriptor.nestedEntityDescriptors()
                .forEach((foreignKeyColumn, nestedEntityDescriptor) -> {
                    template.update(
                            SqlPatterns.delete(nestedEntityDescriptor),
                            NamedParams.idAsMap(
                                    nestedEntityIds.get(foreignKeyColumn),
                                    nestedEntityDescriptor,
                                    false
                            )
                    );
                    log.debug("SUCCESS DELETE query. Entity(tableName): " + nestedEntityDescriptor.tableName());
                });
    }

    public <T> T selectByIdQuery(long id, EntityDescriptor<T> entityDescriptor, SingleEntityExtractor<T> entityExtractor) {
        var entity = template.query(
                SqlPatterns.selectSingleBy(
                        entityDescriptor,
                        Set.of(
                                Pair.of(
                                        Columns.id(entityDescriptor, true),
                                        "="
                                )
                        )
                ),
                NamedParams.idAsMap(id, entityDescriptor, true),
                entityExtractor
        );
        log.debug("SUCCESS SELECT BY ID query. Entity(tableName): " + entityDescriptor.tableName());
        return entity;
    }

    public <T> T selectByQuery(
            Map<Pair<String, String>, Object> filterColumns,
            EntityDescriptor<T> entityDescriptor,
            SingleEntityExtractor<T> singleEntityExtractor
    ) {
        filterColumns = protectFilters(filterColumns);
        validateUserDefinedColumns(
                entityDescriptor,
                filterColumns.keySet()
                        .stream()
                        .map(Pair::getKey)
                        .collect(Collectors.toList())
        );

        var entity = template.query(
                SqlPatterns.selectSingleBy(
                        entityDescriptor,
                        filterColumns.keySet()
                ),
                NamedParams.columnsAsMap(
                        filterColumns.entrySet()
                                .stream()
                                .map(entry -> Pair.of(
                                        entry.getKey().getKey(),
                                        entry.getValue()
                                ))
                                .collect(Collectors.toMap(Pair::getKey, Pair::getValue))
                ),
                singleEntityExtractor
        );
        log.debug("SUCCESS SELECT BY query. Entity(tableName): " + entityDescriptor.tableName());
        return entity;
    }

    public <T> List<T> selectPageQuery(
            Map<Pair<String, String>, Object> filterColumns,
            List<String> orderByColumns,
            int count,
            int offset,
            EntityDescriptor<T> entityDescriptor,
            SingleEntityExtractor<T> entityExtractor
    ) {
        filterColumns = protectFilters(filterColumns);
        orderByColumns = protectOrderBy(orderByColumns);
        validateUserDefinedColumns(
                entityDescriptor,
                filterColumns.keySet()
                        .stream()
                        .map(Pair::getKey)
                        .collect(Collectors.toList())
        );
        validateUserDefinedColumns(entityDescriptor, orderByColumns);

        var result = template.query(
                SqlPatterns.selectMultiple(
                        entityDescriptor,
                        filterColumns.keySet(),
                        orderByColumns,
                        count,
                        offset
                ),
                NamedParams.columnsAsMap(
                        filterColumns.entrySet()
                                .stream()
                                .map(entry -> Pair.of(
                                        entry.getKey().getKey(),
                                        entry.getValue()
                                ))
                                .collect(Collectors.toMap(Pair::getKey, Pair::getValue))
                ),
                new MultipleEntityExtractor<>(entityExtractor)
        );
        log.debug("SUCCESS SELECT PAGEABLE query. Entity(tableName): " + entityDescriptor.tableName());
        return result;
    }

    private void validateUserDefinedColumns(
            EntityDescriptor<?> entityDescriptor,
            Collection<String> columns
    ) {
        var allColumns = entityDescriptor.allColumns(true);
        columns.forEach(column -> {
            if (!allColumns.contains(column)) throw new FineQueryException("Column is not belong to " +
                    "entity descriptor: " + column + ". Entity(tableName): " + entityDescriptor.tableName());
        });
    }

    private Map<Pair<String, String>, Object> protectFilters(Map<Pair<String, String>, Object> filterColumns) {
        return filterColumns.entrySet()
                .stream()
                .map(entry -> Pair.of(
                        Pair.of(
                                protectColumn(entry.getKey().getKey()),
                                entry.getKey().getValue()
                        ),
                        entry.getValue()
                ))
                .collect(Collectors.toMap(Pair::getKey, Pair::getValue));
    }

    private List<String> protectOrderBy(List<String> orderByColumns) {
        return orderByColumns
                .stream()
                .map(this::protectColumn)
                .collect(Collectors.toList());
    }

    private String protectColumn(String column) {
        return column.replaceAll(",", "\\,")
                .replaceAll(";", "\\;")
                .replaceAll(":", "\\:")
                .replaceAll("'", "\\'")
                .replaceAll("\\?", "\\?");
    }

    private <T> Map<String, Long> provideForeignKeysMapCreateNestedEntitiesIfNeeded(
            T entity,
            EntityDescriptor<T> entityDescriptor
    ) {
        var foreignKeys = new HashMap<String, Long>();
        entityDescriptor.nestedEntityDescriptors()
                .forEach((foreignKeyColumn, nestedEntityDescriptor) -> {
                    var rawNestedEntityDescriptor = (EntityDescriptor) nestedEntityDescriptor;
                    var nestedEntity = entityDescriptor.extractValue(foreignKeyColumn, entity);

                    if (nestedEntity == null) {
                        throw new FineUnsupportedException("Fine currently doesn't support nullable nested entities. " +
                                "Problem nested entity table name: " + nestedEntityDescriptor.tableName());
                    }

                    var nestedEntityId = rawNestedEntityDescriptor.extractValue(
                            nestedEntityDescriptor.idColumn(),
                            nestedEntity
                    );

                    if (nestedEntityId.getClass() == Long.class) {
                        var longNestedEntityId = (Long) nestedEntityId;

                        if (longNestedEntityId == 0) {
                            longNestedEntityId = createQuery(nestedEntity, rawNestedEntityDescriptor);
                        }

                        foreignKeys.put(foreignKeyColumn, longNestedEntityId);
                        return;
                    }

                    throw new FineUnsupportedException("Fine currently support only Long id values. " +
                            "Problem entity with tableName: " + nestedEntityDescriptor.tableName());
                });
        return foreignKeys;
    }
}
