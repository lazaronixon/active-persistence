package com.activepersistence.service;

import com.activepersistence.service.arel.nodes.JpqlLiteral;

public class Arel {

    public static JpqlLiteral jpql(String rawJpql) {
        return new JpqlLiteral(rawJpql);
    }

}
