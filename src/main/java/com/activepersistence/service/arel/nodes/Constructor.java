package com.activepersistence.service.arel.nodes;

import com.activepersistence.service.arel.Entity;
import java.util.List;

public class Constructor extends Node {

    private final JoinSource source;

    private final List<SqlLiteral> projections;

    public Constructor(JoinSource source, List<SqlLiteral> projections) {
        this.source = source;
        this.projections = projections;
    }

    public List<SqlLiteral> getProjections() {
        return projections;
    }

    public String getClassName() {
        return ((Entity) source.getLeft()).getClassName();
    }

}
