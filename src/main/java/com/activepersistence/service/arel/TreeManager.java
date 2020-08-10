package com.activepersistence.service.arel;

import com.activepersistence.service.arel.collectors.JPQLString;
import com.activepersistence.service.arel.visitors.Visitable;

public abstract class TreeManager {

    public abstract Visitable getAst();

    public String toJpql() {
        var collector = new JPQLString();
        collector     = Entity.visitor.accept(getAst(), collector);
        return collector.getValue();
    }

}
