package com.activepersistence.service.arel.nodes;

public class BindParam extends Node {

    private final Object value;

    public BindParam(Object value) {
        this.value = value;
    }

    public Object getValue() {
        return value;
    }

}
