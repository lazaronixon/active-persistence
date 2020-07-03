package com.activepersistence.service.arel.nodes;

import com.activepersistence.service.arel.Entity;
import java.util.ArrayList;
import java.util.List;

public class DeleteStatement extends Node {

    private Entity relation;

    private List<Node> wheres = new ArrayList();

    private List<Node> orders = new ArrayList();

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

    public List<Node> getOrders() {
        return orders;
    }

    public void setOrders(List<Node> orders) {
        this.orders = orders;
    }

}
