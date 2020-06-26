package com.activepersistence.service.arel.nodes;

import com.activepersistence.service.arel.Entity;
import com.activepersistence.service.arel.Source;

public class EntityAlias implements Source {

    private final Source relation;
    private final String name;

    public EntityAlias(Source relation, String name) {
        this.relation = relation;
        this.name = name;
    }

    public Source getRelation() {
        return relation;
    }

    public String getName() {
        return name;
    }

    public String toJpql() {
        StringBuilder collector = new StringBuilder();
        collector = Entity.visitor.accept(this, collector);
        return collector.toString();
    }

    @Override
    public String getClassName() {
        return relation.getClassName();
    }

    @Override
    public String getAlias() {
        return relation.getAlias();
    }

}
