package com.activepersistence.service.arel.nodes;

import com.activepersistence.service.arel.Source;
import java.util.ArrayList;
import java.util.List;

public class JoinSource extends Node {

    private Source left;
    private final List<SqlLiteral> right;

    public JoinSource() {
        this.right = new ArrayList();
    }

    public Source getLeft() {
        return left;
    }

    public void setLeft(Source left) {
        this.left = left;
    }

    public List<SqlLiteral> getRight() {
        return right;
    }

}
