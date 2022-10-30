package ru.yofik.athena.common.storage.fine.entityDescriptor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public final class EntityDescriptorBuilder<T> {
    private final String tableName;
    private final String idColumn;
    private final Function<T, Long> idExtractor;
    private final List<String> domainColumns;
    private final Map<String, Function<T, ?>> valueExtractors;
    private final Map<String, EntityDescriptor<?>> nestedEntityDescriptors;


    public EntityDescriptorBuilder(
            String tableName,
            String idColumn,
            Function<T, Long> idExtractor
    ) {
        this.tableName = tableName;
        this.idColumn = idColumn;
        this.idExtractor = idExtractor;
        this.domainColumns = new ArrayList<>();
        this.valueExtractors = new HashMap<>();
        this.nestedEntityDescriptors = new HashMap<>();
    }


    public EntityDescriptorBuilder<T> domainColumn(String column, Function<T, ?> valueExtractor) {
        domainColumns.add(column);
        valueExtractors.put(column, valueExtractor);
        return this;
    }

    public EntityDescriptorBuilder<T> nestedEntity(
            String foreignKeyColumn,
            EntityDescriptor<?> nestedEntityDescriptor,
            Function<T, ?> nestedEntityExtractor
    ) {
        domainColumns.add(foreignKeyColumn);
        valueExtractors.put(foreignKeyColumn, nestedEntityExtractor);
        nestedEntityDescriptors.put(foreignKeyColumn, nestedEntityDescriptor);
        return this;
    }

    public EntityDescriptor<T> build() {
        var descriptor = new EntityDescriptor<>(
                tableName,
                idColumn,
                idExtractor,
                domainColumns,
                nestedEntityDescriptors
        );
        valueExtractors.forEach(descriptor::registerMapper);
        return descriptor;
    }
}
