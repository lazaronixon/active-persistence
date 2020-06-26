package com.activepersistence.service.arel;

public interface Source {

    public default String getAlias() {
        return "this";
    }

    public String getClassName();

}
