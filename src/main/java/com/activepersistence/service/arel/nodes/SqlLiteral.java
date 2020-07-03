package com.activepersistence.service.arel.nodes;

import com.activepersistence.service.Arel;
import com.activepersistence.service.arel.Source;
import java.util.Arrays;
import java.util.List;
import static java.util.stream.Collectors.toList;

public class SqlLiteral extends Node implements Source {

    private final String value;

    public SqlLiteral(String value) {
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

    public static List<Node> asList(String... values) {
        return Arrays.asList(values).stream().map(Arel::jpql).collect(toList());
    }

    @Override
    public String toString() {
        return value;
    }

}
