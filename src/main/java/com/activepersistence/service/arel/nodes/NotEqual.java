package com.activepersistence.service.arel.nodes;

import com.activepersistence.service.arel.visitors.Visitable;

public class NotEqual extends Node {

    private final Visitable left;

    private final Visitable right;

    public NotEqual(Visitable left, Visitable right) {
        this.left = left;
        this.right = right;
    }

    public Visitable getLeft() {
        return left;
    }

    public Visitable getRight() {
        return right;
    }

}
