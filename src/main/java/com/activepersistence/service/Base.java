package com.activepersistence.service;

import javax.persistence.EntityManager;

public abstract class Base<T> implements Persistence<T>, Querying<T>  {

    private final Class<T> entityClass;

    private Relation<T> relation;

    public Base(Class<T> entityClass) {
        this.entityClass = entityClass;
    }

    @Override
    public abstract EntityManager getEntityManager();

    @Override
    public Class<T> getEntityClass() {
        return entityClass;
    }

    @Override
    public Relation<T> getRelation() {
        return relation != null ? relation : new Relation(this);
    }

    public boolean useDefaultScope() {
        return false;
    }

    public Relation<T> defaultScope() {
        throw new UnsupportedOperationException("defaultScope() must be implemented when useDefaultScope() is true.");
    }

}
