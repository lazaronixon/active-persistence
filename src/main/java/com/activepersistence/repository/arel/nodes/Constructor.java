package com.activepersistence.repository.arel.nodes;

import com.activepersistence.repository.arel.visitors.Visitable;
import java.util.List;

public class Constructor extends Node {

    private final String name;

    private final List<Visitable> projections;

    public Constructor(String name, List<Visitable> projections) {
        this.name = name;
        this.projections = projections;
    }

    public List<Visitable> getProjections() {
        return projections;
    }

    public String getName() {
        return name;
    }

}
