package com.activepersistence.service.arel.nodes;

public class JoinSource extends Node {

    private Object left;

    private Object right;

    public JoinSource() {
    }

    public JoinSource(Object left) {
        this.left = left;
    }

    public Object getLeft() {
        return left;
    }

    public void setLeft(Object left) {
        this.left = left;
    }

    public Object getRight() {
        return right;
    }

    public void setRight(Object right) {
        this.right = right;
    }

}
