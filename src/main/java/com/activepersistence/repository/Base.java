package com.activepersistence.repository;

import com.activepersistence.repository.connectionadapters.JpaAdapter;
import java.lang.reflect.Array;
import java.lang.reflect.ParameterizedType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

public abstract class Base<T> implements Core<T>, Callbacks<T>, Querying<T>, Scoping<T>, Sanitization<T> {

    private final Class entityClass;

    @PersistenceContext
    private EntityManager entityManager;

    public Base() {
        entityClass = resolveEntityClass();
    }

    @Override
    public Class getEntityClass() {
        return entityClass;
    }

    @Override
    public EntityManager getEntityManager() {
        return entityManager;
    }

    @Override
    public Relation<T> getRelation() {
        return Core.super.getRelation();
    }

    @Override
    public Relation<T> all() {
        return Scoping.super.all();
    }

    @Override
    public Class getRealClass() {
        return getClass().getSuperclass();
    }

    public JpaAdapter<T> getConnection() {
        return new JpaAdapter(getEntityManager(), entityClass);
    }

    @Override
    public Relation<T> defaultScope() {
        throw new UnsupportedOperationException();
    }

    //<editor-fold defaultstate="collapsed" desc="Private">
    private Class resolveEntityClass() {
        return (Class) Array.get(getParameterizedType().getActualTypeArguments(), 0);
    }

    private ParameterizedType getParameterizedType() {
        return (ParameterizedType) getRealClass().getGenericSuperclass();
    }
    //</editor-fold>

}
