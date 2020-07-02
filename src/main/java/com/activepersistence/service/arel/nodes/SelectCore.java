package com.activepersistence.service.arel.nodes;

import java.util.ArrayList;
import java.util.List;

public class SelectCore extends Node {

    private JoinSource source;
    private Distinct setQuantifier;
    private Constructor constructor;
    private final List<Node> projections;
    private final List<Node> wheres;
    private final List<Node> groups;
    private final List<Node> havings;

    public SelectCore() {
        this.source         = new JoinSource();
        this.setQuantifier  = null;
        this.constructor    = null;
        this.projections    = new ArrayList();
        this.wheres         = new ArrayList();
        this.groups         = new ArrayList();
        this.havings        = new ArrayList();
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

    public void setConstructor(String name) {
        this.constructor = name != null ? new Constructor(name, this.getProjections()) : null;
    }

    public List<Node> getProjections() {
        return projections;
    }

    public List<Node> getWheres() {
        return wheres;
    }

}
