package com.activepersistence.service.arel.nodes;

public abstract class Join extends Node {

    private final JpqlLiteral path;

    private JpqlLiteral alias;

    private JpqlLiteral constraint;

    public Join(JpqlLiteral path) {
        this.path = path;
    }

    public Join(JpqlLiteral path, JpqlLiteral alias) {
        this.path  = path;
        this.alias = alias;
    }

    public Join(JpqlLiteral path, JpqlLiteral alias, JpqlLiteral constraint) {
        this.path  = path;
        this.alias = alias;
        this.constraint = constraint;
    }

    public JpqlLiteral getPath() {
        return path;
    }

    public JpqlLiteral getAlias() {
        return alias;
    }

    public JpqlLiteral getConstraint() {
        return constraint;
    }

}
