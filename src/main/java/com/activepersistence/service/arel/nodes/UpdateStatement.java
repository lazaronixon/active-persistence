package com.activepersistence.service.arel.nodes;

import com.activepersistence.service.arel.Entity;
import com.activepersistence.service.arel.visitors.Visitable;
import java.util.ArrayList;
import java.util.List;

public class UpdateStatement extends Node {

    private Entity relation;

    private List<Visitable> wheres = new ArrayList();

    private List<Visitable> orders = new ArrayList();

    private List<Visitable> values = new ArrayList();

    public Entity getRelation() {
        return relation;
    }

    public void setRelation(Entity relation) {
        this.relation = relation;
    }

    public List<Visitable> getWheres() {
        return wheres;
    }

    public void setWheres(List<Visitable> wheres) {
        this.wheres = wheres;
    }

    public List<Visitable> getOrders() {
        return orders;
    }

    public void setOrders(List<Visitable> orders) {
        this.orders = orders;
    }

    public List<Visitable> getValues() {
        return values;
    }

    public void setValues(List<Visitable> values) {
        this.values = values;
    }

}
