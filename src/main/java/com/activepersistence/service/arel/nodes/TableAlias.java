package com.activepersistence.service.arel.nodes;

import com.activepersistence.service.arel.Source;

public class TableAlias extends Node implements Source {

    private final SelectStatement relation;
    private final String name;

    public TableAlias(SelectStatement relation, String name) {
        this.relation = relation;
        this.name = name;
    }

    public SelectStatement getRelation() {
        return relation;
    }

    public String getName() {
        return name;
    }

}
