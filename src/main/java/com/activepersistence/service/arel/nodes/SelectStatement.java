package com.activepersistence.service.arel.nodes;

import java.util.ArrayList;
import java.util.List;

public class SelectStatement extends Node {

    private final SelectCore core = new SelectCore();

    private final List<SqlLiteral> orders = new ArrayList();

    public SelectCore getCore() {
        return core;
    }

    public List<SqlLiteral> getOrders() {
        return orders;
    }

    public void addOrders(List<SqlLiteral> expr) {
        this.orders.addAll(expr);
    }

}
