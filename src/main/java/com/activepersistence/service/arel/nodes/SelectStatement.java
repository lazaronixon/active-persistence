package com.activepersistence.service.arel.nodes;

import java.util.ArrayList;
import java.util.List;

public class SelectStatement extends Node {

    private final List<SelectCore> cores;

    private final List<SqlLiteral> orders;

    public SelectStatement() {
       cores  = List.of(new SelectCore());
       orders = new ArrayList();
    }


    public List<SqlLiteral> getOrders() {
        return orders;
    }

    public List<SelectCore> getCores() {
        return cores;
    }

    public SelectCore getLastCore() {
        return cores.get(cores.size() - 1);
    }

    public void addOrders(List<SqlLiteral> expr) {
        this.orders.addAll(expr);
    }

}
