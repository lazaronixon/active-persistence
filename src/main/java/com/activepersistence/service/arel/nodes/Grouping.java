package com.activepersistence.service.arel.nodes;

import com.activepersistence.service.arel.visitors.Visitable;

public class Grouping extends Node {

    private final Visitable value;

    public Grouping(Visitable value) {
        this.value = value;
    }

    public Visitable getValue() {
        return value;
    }

}
