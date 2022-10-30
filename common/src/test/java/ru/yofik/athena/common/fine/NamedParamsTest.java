package ru.yofik.athena.common.fine;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yofik.athena.common.storage.fine.NamedParams;
import ru.yofik.athena.common.storage.fine.entityDescriptor.EntityDescriptor;
import ru.yofik.athena.common.storage.fine.entityDescriptor.EntityDescriptorBuilder;
import ru.yofik.athena.common.fine.testEntity.TestEntity;
import ru.yofik.athena.common.fine.testEntity.TestNestedEntityA;
import ru.yofik.athena.common.fine.testEntity.TestNestedEntityB;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class NamedParamsTest {
    private EntityDescriptor<TestEntity> entityDescriptor;
    private TestEntity testEntity;

    @BeforeEach
    void setup() {
        entityDescriptor = new EntityDescriptorBuilder<>(
                "table",
                "id",
                TestEntity::getId
        ).domainColumn("name", TestEntity::getName)
                .nestedEntity(
                        "nested_a_id",
                        new EntityDescriptorBuilder<>(
                                "table_a",
                                "id",
                                TestNestedEntityA::getId
                        )
                                .domainColumn("test_a", TestNestedEntityA::getTestA)
                                .nestedEntity(
                                        "nested_b_id",
                                        new EntityDescriptorBuilder<>(
                                                "table_b",
                                                "id",
                                                TestNestedEntityB::getId
                                        ).domainColumn("test_b", TestNestedEntityB::getTestB).build(),
                                        TestNestedEntityA::getNestedB
                                )
                                .build(),
                        TestEntity::getNestedA
                )
                .build();
        testEntity = new TestEntity(
                1,
                "qwerty",
                new TestNestedEntityA(
                        2,
                        "asd",
                        new TestNestedEntityB(
                                3,
                                "zxc"
                        )
                )
        );
    }

    @Test
    void idAsMapReturnsCorrectly() {
        var result = NamedParams.idAsMap(
                testEntity.getId(),
                entityDescriptor,
                true
        );

        assertThat(result.getValue("table.id")).isEqualTo(testEntity.getId());

        result = NamedParams.idAsMap(
                testEntity.getId(),
                entityDescriptor,
                false
        );

        assertThat(result.getValue("id")).isEqualTo(testEntity.getId());
    }

    @Test
    void selfDomainAsMapReturnsCorrectly() {
        var result = NamedParams.selfDomainAsMap(testEntity, entityDescriptor, true);

        assertThat(result.getValue("table.name")).isEqualTo(testEntity.getName());
        assertThat(result.getValue("table.nested_a_id")).isEqualTo(testEntity.getNestedA().getId());

        result = NamedParams.selfDomainAsMap(testEntity, entityDescriptor, false);

        assertThat(result.getValue("name")).isEqualTo(testEntity.getName());
        assertThat(result.getValue("nested_a_id")).isEqualTo(testEntity.getNestedA().getId());
    }

    @Test
    void selfAllAsMapReturnsCorrectly() {
        var result = NamedParams.selfAllAsMap(testEntity, entityDescriptor, true);

        assertThat(result.getValue("table.id")).isEqualTo(testEntity.getId());
        assertThat(result.getValue("table.name")).isEqualTo(testEntity.getName());
        assertThat(result.getValue("table.nested_a_id")).isEqualTo(testEntity.getNestedA().getId());

        result = NamedParams.selfAllAsMap(testEntity, entityDescriptor, false);

        assertThat(result.getValue("id")).isEqualTo(testEntity.getId());
        assertThat(result.getValue("name")).isEqualTo(testEntity.getName());
        assertThat(result.getValue("nested_a_id")).isEqualTo(testEntity.getNestedA().getId());
    }

    @Test
    void selfDomainWithProvidedForeignKeysAsMapReturnsCorrectly() {
        var result = NamedParams.selfDomainWithProvidedForeignKeysAsMap(
                testEntity,
                entityDescriptor,
                Map.of("nested_a_id", 10L),
                true
        );

        assertThat(result.getValue("table.name")).isEqualTo(testEntity.getName());
        assertThat(result.getValue("table.nested_a_id")).isEqualTo(10L);

        result = NamedParams.selfDomainWithProvidedForeignKeysAsMap(
                testEntity,
                entityDescriptor,
                Map.of("nested_a_id", 10L),
                false
        );

        assertThat(result.getValue("name")).isEqualTo(testEntity.getName());
        assertThat(result.getValue("nested_a_id")).isEqualTo(10L);
    }

    @Test
    void idAsStringReturnsCorrectly() {
        var result = NamedParams.idAsString(entityDescriptor, true);

        assertThat(result).isEqualTo(":table.id");

        result = NamedParams.idAsString(entityDescriptor, false);

        assertThat(result).isEqualTo(":id");
    }

    @Test
    void selfDomainAsStringReturnsCorreclty() {
        var result = NamedParams.selfDomainAsString(entityDescriptor, true);

        assertThat(result).isEqualTo(":table.name,:table.nested_a_id");

        result = NamedParams.selfDomainAsString(entityDescriptor, false);

        assertThat(result).isEqualTo(":name,:nested_a_id");
    }

    @Test
    void selfAllAsStringReturnsCorrectly() {
        var result = NamedParams.selfAllAsString(entityDescriptor, true);

        assertThat(result).isEqualTo(":table.id,:table.name,:table.nested_a_id");

        result = NamedParams.selfAllAsString(entityDescriptor, false);

        assertThat(result).isEqualTo(":id,:name,:nested_a_id");
    }
}
