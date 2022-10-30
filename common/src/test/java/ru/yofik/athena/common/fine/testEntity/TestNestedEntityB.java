package ru.yofik.athena.common.fine.testEntity;

public class TestNestedEntityB {
    private final long id;
    private final String testB;

    public TestNestedEntityB(long id, String testB) {
        this.id = id;
        this.testB = testB;
    }

    public long getId() {
        return id;
    }

    public String getTestB() {
        return testB;
    }
}
