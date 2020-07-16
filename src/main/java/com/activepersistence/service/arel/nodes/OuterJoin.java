package com.activepersistence.service.arel.nodes;

public class OuterJoin extends Join {

    public OuterJoin(JpqlLiteral path, JpqlLiteral alias) {
        super(path, alias);
    }

    public OuterJoin(JpqlLiteral path, JpqlLiteral alias, JpqlLiteral constraint) {
        super(path, alias, constraint);
    }

}
