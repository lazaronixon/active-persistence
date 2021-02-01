package com.activepersistence.repository.arel.nodes;

public class Assignment extends Node {

    private final JpqlLiteral field;
    private final Object value;

    public Assignment(JpqlLiteral field, Object value) {
        this.field = field;
        this.value = value;
    }

    public JpqlLiteral getField() {
        return field;
    }

    public Object getValue() {
        return value;
    }

}
