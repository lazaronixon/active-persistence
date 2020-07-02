package com.activepersistence.service.arel.nodes;

import static com.activepersistence.service.Arel.jpql;
import com.activepersistence.service.arel.Source;
import java.util.List;
import static java.util.stream.Collectors.toList;
import static java.util.stream.IntStream.range;

public class SqlLiteral extends Node implements Source {

    private final String value;

    public SqlLiteral(String value) {
        this.value = value;
    }

    public static List<Node> of(String... values) {
        return range(0, values.length).mapToObj(i -> jpql(values[i])).collect(toList());
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
