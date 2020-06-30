package com.activepersistence.service.relation;

public class FromClause {

    private final Object value;

    private String name;

    public FromClause(Object value) {
        this.value = value;
    }

    public FromClause(Object value, String name) {
        this.value = value;
        this.name = name;
    }

    public Object getValue() {
        return value;
    }

    public String getName() {
        return name;
    }

}
