package com.activepersistence.service.arel.nodes;

import com.activepersistence.service.arel.Entity;
import java.util.ArrayList;
import java.util.List;

public class SelectCore {

    private Entity source;
    private Distinct setQuantifier;
    private Constructor setConstructor;
    private final List<SqlLiteral> projections;
    private final List<SqlLiteral> joins;
    private final List<SqlLiteral> wheres;
    private final List<SqlLiteral> groups;
    private final List<SqlLiteral> havings;

    public SelectCore() {
        this.source         = null;
        this.setQuantifier  = null;
        this.setConstructor = null;
        this.projections    = new ArrayList();
        this.joins          = new ArrayList();
        this.wheres         = new ArrayList();
        this.groups         = new ArrayList();
        this.havings        = new ArrayList();
    }

    public Distinct getSetQuantifier() {
        return setQuantifier;
    }

    public Constructor getSetConstructor() {
        return setConstructor;
    }

    public List<SqlLiteral> getGroups() {
        return groups;
    }

    public List<SqlLiteral> getHavings() {
        return havings;
    }

    public List<SqlLiteral> getJoins() {
        return joins;
    }

    public Entity getSource() {
        return source;
    }

    public void setSource(Entity source) {
        this.source = source;
    }

    public void addProjections(List<SqlLiteral> projections) {
        this.projections.addAll(projections);
    }

    public void addJoin(SqlLiteral join) {
        this.joins.add(join);
    }

    public void addWhere(SqlLiteral condition) {
        this.wheres.add(condition);
    }

    public void addGroups(List<SqlLiteral> fields) {
        this.groups.addAll(fields);
    }

    public void addHaving(SqlLiteral condition) {
        this.havings.add(condition);
    }

    public void setDistinct(boolean value) {
        this.setQuantifier = value ? new Distinct() : null;
    }

    public void setConstructor(boolean value) {
        this.setConstructor = value ? new Constructor(source.getName(), this.getProjections()) : null;
    }

    public List<SqlLiteral> getProjections() {
        return projections;
    }

    public List<SqlLiteral> getWheres() {
        return wheres;
    }

}
