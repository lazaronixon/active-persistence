package com.activepersistence.service.arel.nodes;

public class Literalized extends Node {

    private final Object value;

    public Literalized(Object value) {
        this.value = value;
    }

    public Object getValue() {
        return value;
    }

}
