package com.activepersistence.service.arel.nodes;

import static com.activepersistence.service.Arel.jpql;

public class Assignment extends Node {

    private final JpqlLiteral column;
    private final JpqlLiteral value;

    public Assignment(JpqlLiteral column, JpqlLiteral value) {
        this.column = column;
        this.value  = value;
    }

    public Assignment(String column, String value) {
        this.column = jpql(column);
        this.value  = jpql(value);
    }

    public JpqlLiteral getColumn() {
        return column;
    }

    public JpqlLiteral getValue() {
        return value;
    }

}
