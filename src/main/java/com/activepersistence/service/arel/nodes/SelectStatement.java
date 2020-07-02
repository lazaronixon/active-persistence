package com.activepersistence.service.arel.nodes;

import com.activepersistence.service.arel.Entity;
import java.util.ArrayList;
import java.util.List;

public class SelectStatement extends Node {

    private final SelectCore core;

    private final List<Node> orders;

    public SelectStatement(Entity entity) {
        this.core   = new SelectCore(entity);
        this.orders = new ArrayList();
    }

    public SelectCore getCore() {
        return core;
    }

    public List<Node> getOrders() {
        return orders;
    }

}
