package ru.yofik.athena.common.storage.fine;

import org.apache.commons.lang3.tuple.Pair;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import ru.yofik.athena.common.storage.fine.entityDescriptor.EntityDescriptor;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

final class NamedParams {
    public static <T> MapSqlParameterSource selfAllAsMap(T entity, EntityDescriptor<T> entityDescriptor, boolean withTable) {
        return asMapTemplate(entity, entityDescriptor, EntityDescriptor::selfAllColumns, withTable);
    }

    public static <T> MapSqlParameterSource selfDomainAsMap(T entity, EntityDescriptor<T> entityDescriptor, boolean withTable) {
        return asMapTemplate(entity, entityDescriptor, EntityDescriptor::selfColumnsWithoutId, withTable);
    }

    public static <T> MapSqlParameterSource selfDomainWithProvidedForeignKeysAsMap(
            T entity,
            EntityDescriptor<T> entityDescriptor,
            Map<String, Long> providedForeignKeys,
            boolean withTable
    ) {
        var normalizedProvidedForeignKeys = providedForeignKeys.entrySet()
                .stream()
                .map(entry -> Pair.of(
                        entry.getKey(),
                        entry.getValue()
                ))
                .collect(Collectors.toMap(Pair::getKey, Pair::getValue));

        var mapSqlParameterSource = new MapSqlParameterSource();
        entityDescriptor.selfColumnsWithoutId()
                .forEach(column -> {
                    if (entityDescriptor.nestedEntityDescriptors().containsKey(column)) {
                        if (!normalizedProvidedForeignKeys.containsKey(column)) {
                            throw new RuntimeException("Fine Dao hasn't generated foreign key: " + column);
                        }

                        mapSqlParameterSource.addValue(
                                withTable ? entityDescriptor.withTable(column) : column,
                                normalizedProvidedForeignKeys.get(column)
                        );
                        return;
                    }

                    if (entityDescriptor.hasMapper(column)) {
                        mapSqlParameterSource.addValue(
                                withTable ? entityDescriptor.withTable(column) : column,
                                entityDescriptor.extractValue(column, entity)
                        );
                    }
                });
        return mapSqlParameterSource;
    }

    public static <T, R> MapSqlParameterSource idAsMap(R idValue, EntityDescriptor<T> entityDescriptor, boolean withTable) {
        return new MapSqlParameterSource()
                .addValue(
                        Columns.id(entityDescriptor, withTable),
                        idValue
                );
    }

    public static <T> String selfAllAsString(EntityDescriptor<T> entityDescriptor, boolean withTable) {
        return asStringTemplate(entityDescriptor, EntityDescriptor::selfAllColumns, withTable);
    }

    public static <T> String selfDomainAsString(EntityDescriptor<T> entityDescriptor, boolean withTable) {
        return asStringTemplate(entityDescriptor, EntityDescriptor::selfColumnsWithoutId, withTable);
    }

    public static <T> String idAsString(EntityDescriptor<T> entityDescriptor, boolean withTable) {
        return ":" + Columns.id(entityDescriptor, withTable);
    }

    public static <T> MapSqlParameterSource columnsAsMap(
            Map<String, Object> columns
    ) {
        var mapSqlParameterSource = new MapSqlParameterSource();
        columns.forEach(mapSqlParameterSource::addValue);
        return mapSqlParameterSource;
    }

    private static <T> String asStringTemplate(
            EntityDescriptor<T> entityDescriptor,
            Function<EntityDescriptor<T>, List<String>> columnsExtractor,
            boolean withTable
    ) {
        return columnsExtractor.apply(entityDescriptor)
                .stream()
                .map(column -> ":" + (withTable ? entityDescriptor.withTable(column) : column))
                .collect(Collectors.joining(","));
    }

    private static <T> MapSqlParameterSource asMapTemplate(
            T entity,
            EntityDescriptor<T> entityDescriptor,
            Function<EntityDescriptor<T>, Collection<String>> columnsExtractor,
            boolean withTable
    ) {
        var mapSqlParameterSource = new MapSqlParameterSource();
        columnsExtractor.apply(entityDescriptor)
                .forEach(column -> {
                    if (entityDescriptor.hasMapper(column)) {
                        if (entityDescriptor.nestedEntityDescriptors().containsKey(column)) {
                            // raw type, because extractValue cannot use captre of ? param as entity
                            var nestedEntityDescriptor = (EntityDescriptor) entityDescriptor.nestedEntityDescriptors().get(column);
                            mapSqlParameterSource.addValue(
                                    withTable ? entityDescriptor.withTable(column) : column,
                                    nestedEntityDescriptor.extractValue(
                                            nestedEntityDescriptor.idColumn(),
                                            entityDescriptor.extractValue(
                                                    column,
                                                    entity
                                            )
                                    )
                            );
                        } else {
                            mapSqlParameterSource.addValue(
                                    withTable ? entityDescriptor.withTable(column) : column,
                                    entityDescriptor.extractValue(column, entity)
                            );
                        }
                    }
                });
        return mapSqlParameterSource;
    }
}
