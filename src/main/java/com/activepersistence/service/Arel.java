package com.activepersistence.service;

import com.activepersistence.service.arel.nodes.JpqlLiteral;
import com.activepersistence.service.arel.nodes.StringJoin;
import static java.util.Arrays.asList;
import java.util.List;
import static java.util.stream.Collectors.toList;

public class Arel {

    public static JpqlLiteral jpql(String rawJpql) {
        return new JpqlLiteral(rawJpql);
    }

    public static List<JpqlLiteral> jpqlList(String[] rawJpqls) {
        return asList(rawJpqls).stream().map(Arel::jpql).collect(toList());
    }

    public static StringJoin createStringJoin(String to) {
        return new StringJoin(jpql(to));
    }

}
