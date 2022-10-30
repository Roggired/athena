package ru.yofik.athena.common.storage.fine;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.Pair;
import ru.yofik.athena.common.storage.fine.entityDescriptor.EntityDescriptor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Slf4j
final class SqlPatterns {
    public static String insert(EntityDescriptor<?> entityDescriptor) {
        var sql = String.format(
                "INSERT INTO %s(%s) VALUES(%s) RETURNING id",
                entityDescriptor.tableName(),
                Columns.selfDomain(entityDescriptor, false),
                NamedParams.selfDomainAsString(entityDescriptor, false)
        );
        log.debug(sql);
        return sql;
    }

    public static String update(EntityDescriptor<?> entityDescriptor) {
        var sql = String.format(
                "UPDATE %s SET %s WHERE %s",
                entityDescriptor.tableName(),
                Columns.selfDomainToNamedParams(entityDescriptor, false),
                Columns.idToNamedParam(entityDescriptor, false)
        );
        log.debug(sql);
        return sql;
    }

    public static String delete(EntityDescriptor<?> entityDescriptor) {
        var sql = String.format(
                "DELETE FROM %s WHERE %s",
                entityDescriptor.tableName(),
                Columns.idToNamedParam(entityDescriptor, false)
        );
        log.debug(sql);
        return sql;
    }

    public static String selectForeignKeys(EntityDescriptor<?> entityDescriptor) {
        var sql = String.format(
                "SELECT %s FROM %s WHERE %s",
                String.join(
                        ",",
                        entityDescriptor.nestedEntityDescriptors().keySet()
                ),
                entityDescriptor.tableName(),
                Columns.idToNamedParam(entityDescriptor, false)
        );
        log.debug(sql);
        return sql;
    }

    public static String selectSingleBy(
            EntityDescriptor<?> entityDescriptor,
            Collection<Pair<String, String>> columns
    ) {
        var sql = selectFromLeftJoinWhere(entityDescriptor, columns).toString();
        log.debug(sql);
        return sql;
    }

    private static StringBuilder selectFromLeftJoinWhere(
            EntityDescriptor<?> entityDescriptor,
            Collection<Pair<String, String>> filterColumns
    ) {
        var sqlBuilder = new StringBuilder(
                String.format(
                        "SELECT %s FROM %s ",
                        Columns.all(entityDescriptor, true),
                        entityDescriptor.tableName()
                )
        );

        var entityToNestedPairs = getEntityToNestedPairs(entityDescriptor);
        entityToNestedPairs.forEach(pair -> sqlBuilder.append(
                String.format(
                        "LEFT JOIN %s ON %s ",
                        pair.getValue().getValue().tableName(),
                        pair.getValue().getKey() + "=" + Columns.id(pair.getValue().getValue(), true)
                )
        ));

        if (filterColumns.size() > 0) {
            sqlBuilder.append("WHERE ").append(
                    Columns.columnsToNamedParam(
                            filterColumns,
                            " AND "
                    )
            );
        }
        return sqlBuilder;
    }

    public static String selectMultiple(
            EntityDescriptor<?> entityDescriptor,
            Collection<Pair<String, String>> filterColumns,
            Collection<String> orderByColumns,
            int count,
            int offset
    ) {
        var sqlBuilder = selectFromLeftJoinWhere(entityDescriptor, filterColumns);
        if (orderByColumns.size() > 0) {
            sqlBuilder.append(" ORDER BY ").append(Columns.columns(orderByColumns, ","));
        }
        sqlBuilder.append(" LIMIT ").append(count);
        sqlBuilder.append(" OFFSET ").append(offset == 0? 0 : offset * count);

        var sql = sqlBuilder.toString();
        log.debug(sql);
        return sql;
    }

    private static List<Pair<EntityDescriptor<?>, Pair<String, EntityDescriptor<?>>>> getEntityToNestedPairs(
            EntityDescriptor<?> entityDescriptor
    ) {
        var result = new ArrayList<Pair<EntityDescriptor<?>, Pair<String, EntityDescriptor<?>>>>();
        entityDescriptor.nestedEntityDescriptors()
                .forEach((foreignKeyColumn, nestedEntityDescriptor) -> {
                    result.add(
                            Pair.of(
                                    entityDescriptor,
                                    Pair.of(
                                            foreignKeyColumn,
                                            nestedEntityDescriptor
                                    )
                            )
                    );
                    result.addAll(getEntityToNestedPairs(nestedEntityDescriptor));
                });

        return result;
    }
}
