package com.activepersistence.service.arel.nodes;

public class EntityAlias extends Node {

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

}
