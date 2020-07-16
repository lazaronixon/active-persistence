package com.activepersistence.service.arel.nodes;

import com.activepersistence.service.arel.Source;
import java.util.ArrayList;
import java.util.List;

public class JoinSource extends Node {

    private Source source;
    private List<Join> joins;

    public JoinSource(Source source) {
        this.source  = source;
        this.joins   = new ArrayList();
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
