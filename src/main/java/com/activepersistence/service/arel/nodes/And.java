package com.activepersistence.service.arel.nodes;

import com.activepersistence.service.arel.visitors.Visitable;
import java.util.List;

public class And extends Node {

    private final List<? extends Visitable> children;

    public And(List<? extends Visitable> children) {
        this.children = children;
    }

    public List<? extends Visitable> getChildren() {
        return children;
    }

}