package com.activepersistence.service;

import javax.persistence.EntityManager;

public abstract class Base<T> implements Persistence<T>, Querying<T>, Scoping<T> {

    private final Class<T> entityClass;

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
        return new Relation(this);
    }

    @Override
    public Relation<T> defaultScope() {
        throw new UnsupportedOperationException("not implemented");
    }

    @Override
    public Relation<T> all() {
        return Scoping.super.all();
    }

    @Override
    public Class getRealClass() {
        return getClass().getSimpleName().contains("$Proxy$_$$_WeldSubclass") ? getClass().getSuperclass() : getClass();
    }

}
