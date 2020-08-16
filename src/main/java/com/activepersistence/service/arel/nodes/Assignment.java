package com.activepersistence.service.arel.nodes;

import static com.activepersistence.service.Arel.jpql;

public class Assignment extends Node {

    private final JpqlLiteral field;
    private final Object value;

    public Assignment(String field, Object value) {
        this.field = jpql(field);
        this.value = value;
    }

    public JpqlLiteral getField() {
        return field;
    }

    public Object getValue() {
        return value;
    }

}
