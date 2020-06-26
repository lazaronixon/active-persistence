package com.activepersistence.service.arel.nodes;

import com.activepersistence.service.arel.Entity;
import com.activepersistence.service.arel.FactoryMethods;

public abstract class Node implements FactoryMethods {

    public String toJpql() {
        StringBuilder collector = new StringBuilder();
        collector = Entity.visitor.accept(this, collector);
        return collector.toString();
    }

}
