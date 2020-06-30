package com.activepersistence.service.arel.nodes;

import static com.activepersistence.service.Arel.jpql;

public abstract class Function extends Node {

    private final SqlLiteral expression;
    private SqlLiteral alias;

    private boolean distinct;

    public Function(SqlLiteral expression) {
        this.expression = expression;
    }

    public Function(SqlLiteral expression, boolean distinct) {
        this.expression = expression;
        this.distinct = distinct;
    }

    public Function as(String alias) {
        this.alias = jpql(alias);
        return this;
    }

    public SqlLiteral getExpression() {
        return expression;
    }

    public SqlLiteral getAlias() {
        return alias;
    }

    public boolean isDistinct() {
        return distinct;
    }

}
