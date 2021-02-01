package com.activepersistence.repository.arel.nodes;

import com.activepersistence.repository.arel.visitors.Visitable;
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