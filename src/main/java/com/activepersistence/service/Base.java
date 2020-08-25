package com.activepersistence.service;

import com.activepersistence.service.connectionadapters.JpaAdapter;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

public abstract class Base<T, ID> implements Core<T, ID>, Persistence<T, ID>, Querying<T, ID>, Scoping<T, ID> {

    @PersistenceContext
    private EntityManager entityManager;

    private final Class entityClass;

    public Base(Class entityClass) {
        this.entityClass = entityClass;
    }

    @Override
    public Class getEntityClass() {
        return entityClass;
    }

    @Override
    public EntityManager getEntityManager() {
        return entityManager;
    }

    public JpaAdapter<T> getConnection() {
        return new JpaAdapter(getEntityManager(), entityClass);
    }

    @Override
    public Relation<T, ID> getRelation() {
        return Core.super.getRelation();
    }

    @Override
    public Relation<T, ID> all() {
        return Scoping.super.all();
    }

    @Override
    public Relation<T, ID> defaultScope() {
        throw new UnsupportedOperationException("not implemented");
    }

    @Override
    public Class getRealClass() {
        if (getClass().getSimpleName().contains("$Proxy$_$$_WeldSubclass")) {
            return getClass().getSuperclass();
        } else {
            return getClass();
        }
    }

}
