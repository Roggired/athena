package ru.yofik.athena.common.storage.fine.testEntity;

public class TestEntity {
    private final long id;
    private final String name;
    private final TestNestedEntityA nestedA;

    public TestEntity(long id, String name, TestNestedEntityA nestedA) {
        this.id = id;
        this.name = name;
        this.nestedA = nestedA;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public TestNestedEntityA getNestedA() {
        return nestedA;
    }
}
