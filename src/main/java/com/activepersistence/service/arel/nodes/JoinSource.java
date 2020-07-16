package com.activepersistence.service.arel.nodes;

import com.activepersistence.service.arel.Source;
import java.util.ArrayList;
import java.util.List;

public class JoinSource extends Node {

    private Source source;

    private TableAlias tableAlias;

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

    public TableAlias getTableAlias() {
        return tableAlias;
    }

    public void setTableAlias(TableAlias tableAlias) {
        this.tableAlias = tableAlias;
    }

    public List<Join> getJoins() {
        return joins;
    }

}
