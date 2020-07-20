package com.activepersistence.service.arel.nodes;

import com.activepersistence.service.arel.Entity;
import com.activepersistence.service.arel.visitors.Visitable;

public abstract class Node implements Visitable {

    public String toJpql() {
        var collector = new StringBuilder();
        collector = Entity.visitor.accept(this, collector);
        return collector.toString();
    }

}
