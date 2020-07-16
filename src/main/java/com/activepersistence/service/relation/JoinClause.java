package com.activepersistence.service.relation;

import java.util.Objects;

public class JoinClause {

    private final String path;

    private String alias;

    public JoinClause(String path) {
        this.path = path;
    }

    public JoinClause(String path, String alias) {
        this.path = path;
        this.alias = alias;
    }

    public String getPath() {
        return path;
    }

    public String getAlias() {
        return alias;
    }

}
