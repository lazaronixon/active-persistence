package com.activepersistence.service.arel.nodes;

import static com.activepersistence.service.Arel.jpql;

public class Assignment extends Node {

    private final JpqlLiteral field;
    private final JpqlLiteral value;

    public Assignment(JpqlLiteral field, JpqlLiteral value) {
        this.field = field;
        this.value = value;
    }

    public Assignment(String field, String value) {
        this.field = jpql(field);
        this.value = jpql(value);
    }

    public JpqlLiteral getField() {
        return field;
    }

    public JpqlLiteral getValue() {
        return value;
    }

}
