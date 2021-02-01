package com.activepersistence.repository.arel.nodes;

import static com.activepersistence.repository.Arel.jpql;

public abstract class Function extends Node {

    private final JpqlLiteral expression;

    private JpqlLiteral alias;

    private boolean distinct;

    public Function(JpqlLiteral expression) {
        this.expression = expression;
    }

    public Function(JpqlLiteral expression, boolean distinct) {
        this.expression = expression;
        this.distinct = distinct;
    }

    public Function as(String alias) {
        this.alias = jpql(alias);
        return this;
    }

    public JpqlLiteral getExpression() {
        return expression;
    }

    public JpqlLiteral getAlias() {
        return alias;
    }

    public boolean isDistinct() {
        return distinct;
    }

    public void setDistinct(boolean distinct) {
        this.distinct = distinct;
    }

}
