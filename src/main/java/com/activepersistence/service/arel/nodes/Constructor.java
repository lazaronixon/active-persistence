package com.activepersistence.service.arel.nodes;

import com.activepersistence.service.arel.Source;
import java.util.List;

public class Constructor extends Node {

    private final Source source;

    private final List<SqlLiteral> projections;

    public Constructor(Source source, List<SqlLiteral> projections) {
        this.source = source;
        this.projections = projections;
    }

    public List<SqlLiteral> getProjections() {
        return projections;
    }

    public String getClassName() {
        return source.getClassName();
    }

}
