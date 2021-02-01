package com.activepersistence.repository.arel.nodes;

import com.activepersistence.repository.arel.Entity;
import com.activepersistence.repository.arel.visitors.Visitable;
import java.util.ArrayList;
import java.util.List;

public class UpdateStatement extends Node {

    private Entity relation;

    private List<Visitable> wheres = new ArrayList();

    private List<Visitable> orders = new ArrayList();

    private List<Visitable> values = new ArrayList();

    private int limit;

    private int offset;

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

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

}
