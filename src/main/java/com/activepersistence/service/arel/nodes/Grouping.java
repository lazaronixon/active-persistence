package com.activepersistence.service.arel.nodes;

import static com.activepersistence.service.Arel.jpql;
import com.activepersistence.service.arel.visitors.Visitable;

public class Grouping extends Node {

    private final Visitable value;

    public Grouping(Visitable value) {
        this.value = value;
    }

    public Grouping(String value) {
        this.value = jpql(value);
    }

    public Visitable getValue() {
        return value;
    }

}
