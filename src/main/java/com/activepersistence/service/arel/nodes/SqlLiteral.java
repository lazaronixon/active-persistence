package com.activepersistence.service.arel.nodes;

import static com.activepersistence.service.Arel.jpql;
import com.activepersistence.service.arel.Expressions;
import com.activepersistence.service.arel.Source;
import java.util.List;
import static java.util.stream.Collectors.toList;
import static java.util.stream.IntStream.range;

public class SqlLiteral extends Node implements Source, Expressions {

    private final String value;

    public SqlLiteral(String value) {
        this.value = value;
    }

    public static List<Node> of(String... values) {
        return range(0, values.length).mapToObj(i -> jpql(values[i])).collect(toList());
    }

    @Override
    public SqlLiteral thiz() {
        return this;
    }

    @Override
    public String toString() {
        return value;
    }

}
