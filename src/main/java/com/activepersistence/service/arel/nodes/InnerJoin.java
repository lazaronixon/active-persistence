package com.activepersistence.service.arel.nodes;

public class InnerJoin extends Join {

    public InnerJoin(JpqlLiteral path, JpqlLiteral alias) {
        super(path, alias);
    }

    public InnerJoin(JpqlLiteral path, JpqlLiteral alias, JpqlLiteral constraint) {
        super(path, alias, constraint);
    }

}
