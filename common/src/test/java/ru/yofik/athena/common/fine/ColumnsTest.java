package ru.yofik.athena.common.fine;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yofik.athena.common.storage.fine.Columns;
import ru.yofik.athena.common.storage.fine.entityDescriptor.EntityDescriptor;
import ru.yofik.athena.common.storage.fine.entityDescriptor.EntityDescriptorBuilder;
import ru.yofik.athena.common.fine.testEntity.TestEntity;
import ru.yofik.athena.common.fine.testEntity.TestNestedEntityA;
import ru.yofik.athena.common.fine.testEntity.TestNestedEntityB;

import static org.assertj.core.api.Assertions.assertThat;

public class ColumnsTest {
    private EntityDescriptor<TestEntity> entityDescriptor;

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
                                .domainColumn("name", TestNestedEntityA::getTestA)
                                .nestedEntity(
                                        "nested_b_id",
                                        new EntityDescriptorBuilder<>(
                                                "table_b",
                                                "id",
                                                TestNestedEntityB::getId
                                        ).domainColumn("name", TestNestedEntityB::getTestB).build(),
                                        TestNestedEntityA::getNestedB
                                )
                                .build(),
                        TestEntity::getNestedA
                )
                .build();
    }

    @Test
    void selfAllReturnsCorrectly() {
        var result = Columns.selfAll(entityDescriptor, true);
        assertThat(result).isEqualTo("table.id,table.name,table.nested_a_id");

        result = Columns.selfAll(entityDescriptor, false);
        assertThat(result).isEqualTo("id,name,nested_a_id");
    }

    @Test
    void selfDomainReturnsCorrectly() {
        var result = Columns.selfDomain(entityDescriptor, true);
        assertThat(result).isEqualTo("table.name,table.nested_a_id");

        result = Columns.selfDomain(entityDescriptor, false);
        assertThat(result).isEqualTo("name,nested_a_id");
    }

    @Test
    void idReturnsCorrectly() {
        var result = Columns.id(entityDescriptor, true);
        assertThat(result).isEqualTo("table.id");

        result = Columns.id(entityDescriptor, false);
        assertThat(result).isEqualTo("id");
    }

    @Test
    void allReturnsCorrectly() {
        var result = Columns.all(entityDescriptor, true);
        assertThat(result).isEqualTo("table.id as \"table.id\",table.name as \"table.name\"," +
                "table.nested_a_id as \"table.nested_a_id\",table_a.name as \"table_a.name\"," +
                "table_a.nested_b_id as \"table_a.nested_b_id\",table_b.name as \"table_b.name\"");

        result = Columns.all(entityDescriptor, false);
        assertThat(result).isEqualTo("id,name,nested_a_id,name,nested_b_id,name");
    }

    @Test
    void idToNamedParamsReturnCorrectly() {
        var result = Columns.idToNamedParam(entityDescriptor, true);
        assertThat(result).isEqualTo("table.id=:table.id");

        result = Columns.idToNamedParam(entityDescriptor, false);
        assertThat(result).isEqualTo("id=:id");
    }

    @Test
    void selfDomainToNamedParamsReturnCorrectly() {
        var result = Columns.selfDomainToNamedParams(entityDescriptor, true);
        assertThat(result).isEqualTo("table.name=:table.name,table.nested_a_id=:table.nested_a_id");

        result = Columns.selfDomainToNamedParams(entityDescriptor, false);
        assertThat(result).isEqualTo("name=:name,nested_a_id=:nested_a_id");
    }
}
