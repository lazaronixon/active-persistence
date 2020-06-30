package com.activepersistence.service.arel.nodes;

public class Count extends Function {

    public Count(SqlLiteral expression) {
        super(expression);
    }

    public Count(SqlLiteral expression, boolean distinct) {
        super(expression, distinct);
    }

}
