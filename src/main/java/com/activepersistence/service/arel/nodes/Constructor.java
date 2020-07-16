package com.activepersistence.service.arel.nodes;

import java.util.List;

public class Constructor extends Node {

    private final String name;

    private final List<Node> projections;

    public Constructor(String name, List<Node> projections) {
        this.name = name;
        this.projections = projections;
    }

    public List<Node> getProjections() {
        return projections;
    }

    public String getName() {
        return name;
    }

}
