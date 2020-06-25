package com.activepersistence.service.arel.nodes;

import java.util.List;

public class Constructor {

    private final String className;
    private final List<SqlLiteral> projections;

    public Constructor(String className, List<SqlLiteral> projections) {
        this.className = className;
        this.projections = projections;
    }

    public List<SqlLiteral> getProjections() {
        return projections;
    }

    public String getClassName() {
        return className;
    }

}
