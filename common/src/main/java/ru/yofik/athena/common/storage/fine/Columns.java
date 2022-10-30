package ru.yofik.athena.common.storage.fine;

import org.apache.commons.lang3.tuple.Pair;
import ru.yofik.athena.common.storage.fine.entityDescriptor.EntityDescriptor;

import java.util.Collection;
import java.util.stream.Collectors;

final class Columns {
    public static String selfAll(EntityDescriptor<?> entityDescriptor, boolean withTable) {
        var selfAllColumns = entityDescriptor.selfAllColumns();

        if (withTable) {
            selfAllColumns = selfAllColumns.stream()
                    .map(entityDescriptor::withTable)
                    .collect(Collectors.toList());
        }

        return String.join(",", selfAllColumns);
    }

    public static String selfDomain(EntityDescriptor<?> entityDescriptor, boolean withTable) {
        var selfDomainColumns = entityDescriptor.selfColumnsWithoutId();

        if (withTable) {
            selfDomainColumns = selfDomainColumns.stream()
                    .map(entityDescriptor::withTable)
                    .collect(Collectors.toList());
        }

        return String.join(",", selfDomainColumns);
    }

    public static String id(EntityDescriptor<?> entityDescriptor, boolean withTable) {
        if (withTable) {
            return entityDescriptor.withTable(entityDescriptor.idColumn());
        }

        return entityDescriptor.idColumn();
    }

    public static String all(EntityDescriptor<?> entityDescriptor, boolean withTable) {
        if (withTable) {
            return entityDescriptor.allColumns(true)
                    .stream()
                    .map(column -> column + " as \"" + column + "\"")
                    .collect(Collectors.joining(","));
        } else {
            return String.join(",", entityDescriptor.allColumns(false));
        }
    }

    public static String selfDomainToNamedParams(EntityDescriptor<?> entityDescriptor, boolean withTable) {
        var builder = new StringBuilder();
        var columnsWithoutId = entityDescriptor.selfColumnsWithoutId();

        if (withTable) {
            columnsWithoutId = columnsWithoutId
                    .stream()
                    .map(entityDescriptor::withTable)
                    .collect(Collectors.toList());
        }

        for (int i = 0; i < columnsWithoutId.size(); i++) {
            var column = columnsWithoutId.get(i);
            builder.append(column).append("=").append(":").append(column);

            if (i != columnsWithoutId.size() - 1) {
                builder.append(",");
            }
        }

        return builder.toString();
    }

    public static String idToNamedParam(EntityDescriptor<?> entityDescriptor, boolean withTable) {
        return id(entityDescriptor, withTable) + "=:" + id(entityDescriptor, withTable);
    }

    public static String columnsToNamedParam(
            Collection<Pair<String, String>> columnsWithOperations,
            String joiningString
    ) {
        return columnsWithOperations.stream()
                .map(pair -> {
                    if (pair.getValue().equalsIgnoreCase("like")) {
                        return pair.getKey() + " LIKE :" + pair.getKey() + " || '%'";
                    } else {
                        return pair.getKey() + " " + pair.getValue() + " :" + pair.getKey();
                    }
                })
                .collect(Collectors.joining(joiningString));
    }

    public static String columns(
            Collection<String> columns,
            String joiningString
    ) {
        return String.join(joiningString, columns);
    }
}
