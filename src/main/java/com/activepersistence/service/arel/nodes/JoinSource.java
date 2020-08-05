package com.activepersistence.service.arel.nodes;

import com.activepersistence.service.arel.Source;
import java.util.ArrayList;
import java.util.List;

public class JoinSource extends Node {

    private Source source;

    private final List<Join> joins = new ArrayList();

    public JoinSource(Source source) {
        this.source  = source;
    }

    public Source getSource() {
        return source;
    }

    public void setSource(Source source) {
        this.source = source;
    }

    public List<Join> getJoins() {
        return joins;
    }

}
