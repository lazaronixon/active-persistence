package com.activepersistence.service;

import com.activepersistence.service.connectionadapters.JpaAdapter;
import javax.persistence.EntityManager;

public abstract class Base<T> implements Persistence<T>, Querying<T>, Scoping<T> {

    private static final String PROXY = "$Proxy$_$$_WeldSubclass";

    private final Class entityClass;

    public Base(Class entityClass) {
        this.entityClass = entityClass;
    }

    public Class<T> getEntityClass() {
        return entityClass;
    }

    @Override
    public abstract EntityManager getEntityManager();

    @Override
    public Relation<T> getRelation() {
        return new Relation(this);
    }

    @Override
    public JpaAdapter<T> getConnection() {
        return new JpaAdapter(getEntityManager(), entityClass);
    }

    @Override
    public Relation<T> all() {
        return Scoping.super.all();
    }

    @Override
    public Relation<T> defaultScope() {
        throw new UnsupportedOperationException("not implemented");
    }

    @Override
    public Class getRealClass() {
        return getClass().getSimpleName().contains(PROXY) ? getClass().getSuperclass() : getClass();
    }

}
