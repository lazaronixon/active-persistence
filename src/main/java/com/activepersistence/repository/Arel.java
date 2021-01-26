package com.activepersistence.repository;

import com.activepersistence.repository.arel.nodes.JpqlLiteral;
import com.activepersistence.repository.arel.nodes.StringJoin;
import static java.util.Arrays.stream;
import java.util.List;
import static java.util.stream.Collectors.toList;

public class Arel {

    public static JpqlLiteral jpql(String rawJpql) {
        return new JpqlLiteral(rawJpql);
    }

    public static List<JpqlLiteral> jpqlList(String[] rawJpqls) {
        return stream(rawJpqls).map(v -> jpql(v)).collect(toList());
    }

    public static StringJoin createStringJoin(String to) {
        return new StringJoin(jpql(to));
    }

}
