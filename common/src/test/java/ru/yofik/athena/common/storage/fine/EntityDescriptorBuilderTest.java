package ru.yofik.athena.common.storage.fine;

import org.junit.jupiter.api.Test;
import ru.yofik.athena.common.storage.fine.entityDescriptor.EntityDescriptorBuilder;
import ru.yofik.athena.common.storage.fine.testEntity.TestEntity;
import ru.yofik.athena.common.storage.fine.testEntity.TestNestedEntityA;
import ru.yofik.athena.common.storage.fine.testEntity.TestNestedEntityB;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class EntityDescriptorBuilderTest {
    @Test
    void simpleEntityDescriptorBuilderBuildsCorrectly() {
        var entityDescriptorBuilder = new EntityDescriptorBuilder<>(
                "table_b",
                "id",
                TestNestedEntityB::getId
        ).domainColumn("test_b", TestNestedEntityB::getTestB);

        var testNestedEntityB = new TestNestedEntityB(1, "abcd");
        var result = entityDescriptorBuilder.build();

        assertThat(result.tableName()).isEqualTo("table_b");
        assertThat(result.idColumn()).isEqualTo("id");
        assertThat(result.selfAllColumns()).isEqualTo(List.of("id", "test_b"));
        assertThat(result.selfColumnsWithoutId()).isEqualTo(List.of("test_b"));
        assertThat(result.allColumns(true)).isEqualTo(List.of("table_b.id", "table_b.test_b"));
        assertThat(result.allColumns(false)).isEqualTo(List.of("id", "test_b"));
        assertThat(result.columnsWithoutId(true)).isEqualTo(List.of("table_b.test_b"));
        assertThat(result.columnsWithoutId(false)).isEqualTo(List.of("test_b"));
        assertThat(result.hasMapper("id")).isTrue();
        assertThat(result.hasMapper("test_b")).isTrue();
        assertThat(result.extractValue("id", testNestedEntityB)).isEqualTo(1L);
        assertThat(result.extractValue("test_b", testNestedEntityB)).isEqualTo("abcd");
        assertThat(result.nestedEntityDescriptors()).isEmpty();
    }

    @Test
    void composedEntityDescriptorBuilderBuildCorrectly() {
        var simpleBBuilder = new EntityDescriptorBuilder<>(
                "table_b",
                "id",
                TestNestedEntityB::getId
        ).domainColumn("test_b", TestNestedEntityB::getTestB);
        var nestedBDescriptor = simpleBBuilder.build();

        var simpleABuilder = new EntityDescriptorBuilder<>(
                "table_a",
                "id",
                TestNestedEntityA::getId
        ).domainColumn("test_a", TestNestedEntityA::getTestA)
                .nestedEntity(
                        "nested_b_id",
                        nestedBDescriptor,
                        TestNestedEntityA::getNestedB
                );
        var nestedADescriptor = simpleABuilder.build();

        var builder = new EntityDescriptorBuilder<>(
                "table",
                "id",
                TestEntity::getId
        ).domainColumn("name", TestEntity::getName)
                .nestedEntity("nested_a_id", nestedADescriptor, TestEntity::getNestedA);

        var nestedB = new TestNestedEntityB(3, "zxcv");
        var nestedA = new TestNestedEntityA(2, "abcd", nestedB);
        var testEntity = new TestEntity(
                1,
                "qwerty",
                nestedA
        );
        var result = builder.build();

        assertThat(result.tableName()).isEqualTo("table");
        assertThat(result.idColumn()).isEqualTo("id");
        assertThat(result.selfAllColumns()).isEqualTo(List.of("id", "name", "nested_a_id"));
        assertThat(result.selfColumnsWithoutId()).isEqualTo(List.of("name", "nested_a_id"));
        assertThat(result.allColumns(true)).isEqualTo(List.of("table.id", "table.name", "table.nested_a_id", "table_a.test_a", "table_a.nested_b_id", "table_b.test_b"));
        assertThat(result.allColumns(false)).isEqualTo(List.of("id", "name", "nested_a_id", "test_a", "nested_b_id", "test_b"));
        assertThat(result.columnsWithoutId(true)).isEqualTo(List.of("table.name", "table.nested_a_id", "table_a.test_a", "table_a.nested_b_id", "table_b.test_b"));
        assertThat(result.columnsWithoutId(false)).isEqualTo(List.of("name", "nested_a_id", "test_a", "nested_b_id", "test_b"));
        assertThat(result.hasMapper("id")).isTrue();
        assertThat(result.hasMapper("name")).isTrue();
        assertThat(result.hasMapper("nested_a_id")).isTrue();
        assertThat(result.extractValue("id", testEntity)).isEqualTo(1L);
        assertThat(result.extractValue("name", testEntity)).isEqualTo("qwerty");
        assertThat(result.extractValue("nested_a_id", testEntity)).isEqualTo(nestedA);
        assertThat(result.nestedEntityDescriptors()).isEqualTo(Map.of("nested_a_id", nestedADescriptor));
    }
}
