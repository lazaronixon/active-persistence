package com.activepersistence.service.arel.nodes;

public class Count extends Function {

    public Count(JpqlLiteral expression) {
        super(expression);
    }

    public Count(JpqlLiteral expression, boolean distinct) {
        super(expression, distinct);
    }

}
