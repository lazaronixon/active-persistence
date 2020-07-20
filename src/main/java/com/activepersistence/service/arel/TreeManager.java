package com.activepersistence.service.arel;

import static com.activepersistence.service.arel.Entity.visitor;
import com.activepersistence.service.arel.visitors.Visitable;

public abstract class TreeManager {

    public abstract Visitable getAst();

    public String toJpql() {
        var collector = new StringBuilder();
        collector = visitor.accept(getAst(), collector);
        return collector.toString();
    }

}
