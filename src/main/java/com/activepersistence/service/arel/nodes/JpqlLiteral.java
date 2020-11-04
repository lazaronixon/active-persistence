package com.activepersistence.service.arel.nodes;

import com.activepersistence.service.arel.Source;
import java.util.Objects;

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
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;

        return Objects.equals(this.value, ((JpqlLiteral) obj).value);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(this.value);
    }

    @Override
    public String toString() {
        return value;
    }

}
