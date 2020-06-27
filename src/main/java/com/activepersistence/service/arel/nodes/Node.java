package com.activepersistence.service.arel.nodes;

import com.activepersistence.service.arel.Entity;

public abstract class Node {

    public String toJpql() {
        StringBuilder collector = new StringBuilder();
        collector = Entity.visitor.accept(this, collector);
        return collector.toString();
    }

}
