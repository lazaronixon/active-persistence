package com.activepersistence.service.arel.nodes;

import com.activepersistence.service.arel.Entity;
import java.util.ArrayList;
import java.util.List;

public class UpdateStatement extends Node {

    private Entity relation;

    private List<Node> wheres;

    private List<Node> values;

    public UpdateStatement() {
        this.values = new ArrayList();
        this.wheres = new ArrayList();
    }

    public Entity getRelation() {
        return relation;
    }

    public void setRelation(Entity relation) {
        this.relation = relation;
    }

    public List<Node> getWheres() {
        return wheres;
    }

    public void setWheres(List<Node> wheres) {
        this.wheres = wheres;
    }

    public List<Node> getValues() {
        return values;
    }

    public void setValues(List<Node> values) {
        this.values = values;
    }

}
