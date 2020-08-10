package com.activepersistence.service.arel.nodes;

import static com.activepersistence.service.Arel.jpql;
import com.activepersistence.service.arel.visitors.Visitable;

public class Assignment extends Node {

    private final Visitable field;
    private final Object value;

    public Assignment(Visitable field, Object value) {
        this.field = field;
        this.value = value;
    }

    public Assignment(String field, String value) {
        this.field = jpql(field);
        this.value = jpql(value);
    }

    public Visitable getField() {
        return field;
    }

    public Object getValue() {
        return value;
    }

}
