package com.activepersistence.service.arel.nodes;

import java.util.ArrayList;
import java.util.List;

public class SelectStatement {

    private final SelectCore core = new SelectCore();
    private final List<SqlLiteral> orders = new ArrayList();

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
