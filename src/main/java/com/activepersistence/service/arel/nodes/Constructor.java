package com.activepersistence.service.arel.nodes;

import java.util.List;

public class Constructor extends Node {

    private final Class klass;

    private final List<Node> projections;

    public Constructor(Class klass, List<Node> projections) {
        this.klass = klass;
        this.projections = projections;
    }

    public Class getKlass() {
        return klass;
    }

    public List<Node> getProjections() {
        return projections;
    }

    public String getName() {
        return klass.getName();
    }

}
