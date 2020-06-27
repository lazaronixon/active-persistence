package com.activepersistence.service.arel.nodes;

import java.util.ArrayList;
import java.util.List;

public class SelectStatement extends Node {

    private final SelectCore core;

    private final List<SqlLiteral> orders;

    public SelectStatement() {
       core   = new SelectCore();
       orders = new ArrayList();
    }


    public List<SqlLiteral> getOrders() {
        return orders;
    }

    public SelectCore getCore() {
        return core;
    }

    public void addOrders(List<SqlLiteral> expr) {
        this.orders.addAll(expr);
    }

}
