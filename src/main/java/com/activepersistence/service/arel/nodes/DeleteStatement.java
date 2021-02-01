package com.activepersistence.service.arel.nodes;

import com.activepersistence.service.arel.Entity;
import com.activepersistence.service.arel.visitors.Visitable;
import java.util.ArrayList;
import java.util.List;

public class DeleteStatement extends Node {

    private Entity relation;

    private List<Visitable> wheres = new ArrayList();

    private List<Visitable> orders = new ArrayList();

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
