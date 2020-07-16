package com.activepersistence.service.arel.nodes;

public class TableAlias extends Node {

    private final SelectStatement relation;
    private final JpqlLiteral name;

    public TableAlias(SelectStatement relation, JpqlLiteral name) {
        this.relation = relation;
        this.name = name;
    }

    public SelectStatement getRelation() {
        return relation;
    }

    public JpqlLiteral getName() {
        return name;
    }

}