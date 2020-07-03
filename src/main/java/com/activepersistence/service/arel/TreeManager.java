package com.activepersistence.service.arel;

import com.activepersistence.service.arel.visitors.Visitable;

public abstract class TreeManager {

    public abstract Visitable getAst();

    public String toJpql() {
        StringBuilder collector = new StringBuilder();
        collector = Entity.visitor.accept(getAst(), collector);
        return collector.toString();
    }

}
