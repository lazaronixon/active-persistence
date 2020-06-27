package com.activepersistence.service.arel.nodes;

public class StringJoin extends Node {

    private final SqlLiteral value;

    public StringJoin(SqlLiteral value) {
        this.value = value;
    }

    public SqlLiteral getValue() {
        return value;
    }

}
