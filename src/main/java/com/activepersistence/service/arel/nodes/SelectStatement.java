package com.activepersistence.service.arel.nodes;

import com.activepersistence.service.arel.Entity;
import com.activepersistence.service.arel.visitors.Visitable;
import java.util.ArrayList;
import java.util.List;

public class SelectStatement extends Node {

    private final SelectCore core;

    private final List<Visitable> orders = new ArrayList();

    public SelectStatement(Entity entity) {
        this.core = new SelectCore(entity);
    }

    public SelectCore getCore() {
        return core;
    }

    public List<Visitable> getOrders() {
        return orders;
    }

}
