package com.activepersistence.service.arel.nodes;

import java.util.ArrayList;
import java.util.List;

public class SelectStatement extends Node {

    private final SelectCore core = new SelectCore();

    private final List<Node> orders = new ArrayList();

    public SelectCore getCore() {
        return core;
    }

    public List<Node> getOrders() {
        return orders;
    }

}
