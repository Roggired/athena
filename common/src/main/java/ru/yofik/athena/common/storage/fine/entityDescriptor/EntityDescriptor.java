package ru.yofik.athena.common.storage.fine.entityDescriptor;

import ru.yofik.athena.common.storage.fine.FineDaoConfigurationException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public final class EntityDescriptor<T> {
    private final String tableName;
    private final String idColumn;
    private final List<String> selfAllColumns;
    private final List<String> selfDomainColumns;
    private final Map<String, EntityDescriptor<?>> nestedEntityDescriptors;
    private final Map<String, Function<T, ?>> mappers;


    public EntityDescriptor(
            String tableName,
            String idColumn,
            Function<T, Long> idExtractor,
            List<String> selfDomainColumns,
            Map<String, EntityDescriptor<?>> nestedEntityDescriptors
    ) {
        validateNestedEntityDescriptors(selfDomainColumns, nestedEntityDescriptors);

        this.tableName = tableName;
        this.idColumn = idColumn;
        this.selfDomainColumns = selfDomainColumns;

        this.selfAllColumns = new ArrayList<>();
        selfAllColumns.add(this.idColumn);
        selfAllColumns.addAll(this.selfDomainColumns);

        this.mappers = new HashMap<>();
        mappers.put(this.idColumn, idExtractor);
        this.nestedEntityDescriptors = nestedEntityDescriptors;
    }

    private void validateNestedEntityDescriptors(
            List<String> domainColumns,
            Map<String, EntityDescriptor<?>> nestedEntityDescriptors
    ) {
        nestedEntityDescriptors.keySet().forEach(column -> {
            if (!domainColumns.contains(column)) {
                throw new FineDaoConfigurationException("Each column with associated nested EntityDescriptor must " +
                        "present in domainColumns param. Missing column: " + column);
            }
        });
    }

    public String tableName() {
        return tableName;
    }

    public EntityDescriptor<T> registerMapper(String columnName, Function<T, ?> mapper) {
        if (!selfAllColumns.contains(columnName)) throw new IllegalArgumentException("Column: " + columnName + " is not registered");
        mappers.put(columnName, mapper);
        return this;
    }

    public boolean hasMapper(String columnName) {
        return mappers.containsKey(columnName);
    }

    public String idColumn() {
        return idColumn;
    }

    public List<String> selfAllColumns() {
        return new ArrayList<>(selfAllColumns);
    }

    public List<String> selfColumnsWithoutId() {
        return new ArrayList<>(selfDomainColumns);
    }

    public List<String> allColumns(boolean withTable) {
        var allColumns = selfAllColumns();
        return allColumns
                .stream()
                .flatMap(column -> {
                    if (nestedEntityDescriptors.containsKey(column)) {
                        var nestedEntityDomainColumns = nestedEntityDescriptors.get(column).columnsWithoutId(withTable);
                        nestedEntityDomainColumns.add(0, withTable ? withTable(column) : column);
                        return nestedEntityDomainColumns.stream();
                    } else {
                        return Stream.of(withTable ? withTable(column) : column);
                    }
                })
                .collect(Collectors.toList());
    }

    public List<String> columnsWithoutId(boolean withTable) {
        var result = allColumns(withTable);
        result.remove(0);
        return result;
    }

    public Map<String, EntityDescriptor<?>> nestedEntityDescriptors() {
        return new HashMap<>(nestedEntityDescriptors);
    }

    public Object extractValue(String columnName, T entity) {
        if (!selfAllColumns.contains(columnName)) {
            throw new FineDaoConfigurationException("Column " + columnName + " is not registered");
        }

        if (!mappers.containsKey(columnName)) {
            throw new FineDaoConfigurationException("Mapper for column: " + columnName + " hasn't been registered");
        }

        return mappers.get(columnName).apply(entity);
    }

    public String withTable(String column) {
        return tableName + "." + column;
    }
}
