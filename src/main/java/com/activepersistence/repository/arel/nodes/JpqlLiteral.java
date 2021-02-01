package com.activepersistence.repository.arel.nodes;

import com.activepersistence.repository.arel.Source;

public class JpqlLiteral implements Source {

    private final String value;

    public JpqlLiteral(String value) {
        this.value = value;
    }

    public Count count() {
        return new Count(this);
    }

    public Count count(boolean distinct) {
        return new Count(this, distinct);
    }

    public Sum sum() {
        return new Sum(this);
    }

    public Max maximum() {
        return new Max(this);
    }

    public Min minimum() {
        return new Min(this);
    }

    public Avg average() {
        return new Avg(this);
    }

    @Override
    public String toString() {
        return value;
    }

}
