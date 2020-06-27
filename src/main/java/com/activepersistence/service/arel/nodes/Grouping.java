package com.activepersistence.service.arel.nodes;

public class Grouping extends Node {

    private final SelectStatement expr;

    public Grouping(SelectStatement expr) {
        this.expr = expr;
    }

    public SelectStatement getExpr() {
        return expr;
    }

}
