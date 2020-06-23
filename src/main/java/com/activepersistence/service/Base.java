package com.activepersistence.service;

import javax.persistence.EntityManager;

public abstract class Base<T> implements Persistence<T>, Querying<T>  {

    private final Class<T> entityClass;

    private boolean ignoreDefaultScope;

    public Base(Class<T> entityClass) {
        this.entityClass = entityClass;
    }

    @Override
    public abstract EntityManager getEntityManager();

    @Override
    public Class<T> getEntityClass() { return entityClass; }

    @Override
    public Relation<T> buildRelation() { return new Relation(this); }

    public boolean useDefaultScope() {
        return false;
    }

    public Relation<T> defaultScope() {
        throw new UnsupportedOperationException("defaultScope() must be implemented when useDefaultScope() is true.");
    }

    public boolean shouldIgnoreDefaultScope() {
        return ignoreDefaultScope;
    }

    public void setIgnoreDefaultScope(boolean ignoreDefaultScope) {
        this.ignoreDefaultScope = ignoreDefaultScope;
    }

}
