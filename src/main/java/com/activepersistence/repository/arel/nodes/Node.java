package com.activepersistence.repository.arel.nodes;

import com.activepersistence.repository.arel.Entity;
import com.activepersistence.repository.arel.visitors.Visitable;

public abstract class Node implements Visitable {

    public String toJpql() {
        var collector = new StringBuilder();
        collector     = Entity.visitor.accept(this, collector);
        return collector.toString();
    }

}
