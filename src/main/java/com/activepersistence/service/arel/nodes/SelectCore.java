package com.activepersistence.service.arel.nodes;

import com.activepersistence.service.arel.Entity;
import java.util.ArrayList;
import java.util.List;

public class SelectCore extends Node {

    private JoinSource source;
    private Distinct setQuantifier;
    private Constructor constructor;
    private final List<Node> projections  = new ArrayList();
    private final List<Node> wheres       = new ArrayList();
    private final List<Node> groups       = new ArrayList();
    private final List<Node> havings      = new ArrayList();

    public SelectCore(Entity entity) {
        this.source = new JoinSource(entity);
    }

    public Distinct getSetQuantifier() {
        return setQuantifier;
    }

    public Constructor getConstructor() {
        return constructor;
    }

    public List<Node> getGroups() {
        return groups;
    }

    public List<Node> getHavings() {
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

    public void setConstructor(Class klass) {
        this.constructor = (klass != null ? new Constructor(klass, this.getProjections()) : null);
    }

    public List<Node> getProjections() {
        return projections;
    }

    public List<Node> getWheres() {
        return wheres;
    }

}
