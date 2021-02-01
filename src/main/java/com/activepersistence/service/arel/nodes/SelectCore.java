package com.activepersistence.service.arel.nodes;

import com.activepersistence.service.arel.Entity;
import com.activepersistence.service.arel.visitors.Visitable;
import java.util.ArrayList;
import java.util.List;

public class SelectCore extends Node {

    private final Entity entity;

    private JoinSource source;

    private Distinct setQuantifier;

    private Constructor constructor;

    private final List<Visitable> projections  = new ArrayList();
    private final List<Visitable> wheres       = new ArrayList();
    private final List<Visitable> groups       = new ArrayList();
    private final List<Visitable> havings      = new ArrayList();

    public SelectCore(Entity entity) {
        this.entity = entity;
        this.source = new JoinSource(entity);
    }

    public Distinct getSetQuantifier() {
        return setQuantifier;
    }

    public Constructor getConstructor() {
        return constructor;
    }

    public List<Visitable> getGroups() {
        return groups;
    }

    public List<Visitable> getHavings() {
        return havings;
    }

    public JoinSource getSource() {
        return source;
    }

    public void setSource(JoinSource source) {
        this.source = source;
    }

    public void setDistinct(boolean value) {
        this.setQuantifier = value ? new Distinct() : null;
    }

    public void setConstructor(boolean value) {
        this.constructor = value ? new Constructor(entity.getName(), projections) : null;
    }

    public List<Visitable> getProjections() {
        return projections;
    }

    public List<Visitable> getWheres() {
        return wheres;
    }

}
