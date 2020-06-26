package com.activepersistence.service.arel.nodes;

import java.util.List;
import static java.util.stream.Collectors.toList;
import static java.util.stream.IntStream.range;

public class SqlLiteral extends Node {

    private final String value;

    public SqlLiteral(String value) {
        this.value = value;
    }

    public static List<SqlLiteral> of(String[] values) {
        return range(0, values.length).mapToObj(i -> new SqlLiteral(values[i])).collect(toList());
    }

    @Override
    public String toString() {
        return value;
    }

}
