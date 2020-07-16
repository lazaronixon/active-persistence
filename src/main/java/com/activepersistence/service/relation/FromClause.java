package com.activepersistence.service.relation;

public class FromClause {

    private Object value;

    private String name;

    public FromClause() {
    }

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

    public boolean isEmpty() {
        return value == null;
    }

}