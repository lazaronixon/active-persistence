package com.activepersistence.service.arel.nodes;

import com.activepersistence.service.arel.Entity;
import java.util.ArrayList;
import java.util.List;

public class DeleteStatement extends Node {

    private Entity relation;

    private List<Node> wheres;

    public DeleteStatement(Entity relation) {
        this.relation = relation;
        this.wheres = new ArrayList();
    }

    public DeleteStatement(Entity relation, List<Node> wheres) {
        this.relation = relation;
        this.wheres = wheres;
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

}
