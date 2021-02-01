package com.activepersistence.repository.arel.nodes;

import static com.activepersistence.repository.Arel.jpql;
import com.activepersistence.repository.arel.visitors.Visitable;

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
