package com.activepersistence.service.arel.nodes;

import com.activepersistence.service.arel.Source;

public class EntityAlias extends Node implements Source {

    private final Grouping relation;

    private final String name;

    public EntityAlias(Grouping relation, String name) {
        this.relation = relation;
        this.name = name;
    }

    public Grouping getRelation() {
        return relation;
    }

    public String getName() {
        return name;
    }

    @Override
    public String getClassName() {
        return null;
    }

}
