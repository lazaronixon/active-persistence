package com.activepersistence.service.arel;

import com.activepersistence.service.arel.visitors.Visitable;

public abstract class TreeManager {

    public abstract Visitable getAst();

    public abstract Visitable getCtx();

    public String toJpql() {
        var collector = new StringBuilder();
        collector     = Entity.visitor.accept(getAst(), collector);
        return collector.toString();
    }

}
