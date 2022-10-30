package ru.yofik.athena.common.storage.fine.testEntity;

public class TestNestedEntityA {
    private final long id;
    private final String testA;
    private final TestNestedEntityB nestedB;

    public TestNestedEntityA(long id, String testA, TestNestedEntityB nestedB) {
        this.id = id;
        this.testA = testA;
        this.nestedB = nestedB;
    }

    public long getId() {
        return id;
    }

    public String getTestA() {
        return testA;
    }

    public TestNestedEntityB getNestedB() {
        return nestedB;
    }
}
