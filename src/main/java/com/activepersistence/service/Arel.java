package com.activepersistence.service;

import com.activepersistence.service.arel.nodes.SqlLiteral;

public class Arel {

    public static SqlLiteral jpql(String rawJpql) {
        return new SqlLiteral(rawJpql);
    }

    public static SqlLiteral thiz() {
        return new SqlLiteral("this");
    }

}
