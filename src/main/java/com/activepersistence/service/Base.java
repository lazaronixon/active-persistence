package com.activepersistence.service;

import com.activepersistence.service.callbacks.AfterCreate;
import com.activepersistence.service.callbacks.AfterDestroy;
import com.activepersistence.service.callbacks.AfterSave;
import com.activepersistence.service.callbacks.AfterUpdate;
import com.activepersistence.service.callbacks.BeforeCreate;
import com.activepersistence.service.callbacks.BeforeDestroy;
import com.activepersistence.service.callbacks.BeforeSave;
import com.activepersistence.service.callbacks.BeforeUpdate;
import com.activepersistence.service.connectionadapters.JpaAdapter;
import java.lang.reflect.Array;
import java.lang.reflect.ParameterizedType;
import javax.enterprise.event.Event;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

public abstract class Base<T> implements Core<T>, Callbacks<T>, Querying<T>, Scoping<T> {

    private final Class entityClass;

    @PersistenceContext
    private EntityManager entityManager;

    @Inject @BeforeSave
    private Event<T> beforeSave;

    @Inject @AfterSave
    private Event<T> afterSave;

    @Inject @BeforeCreate
    private Event<T> beforeCreate;

    @Inject @AfterCreate
    private Event<T> afterCreate;

    @Inject @BeforeUpdate
    private Event<T> beforeUpdate;

    @Inject @AfterUpdate
    private Event<T> afterUpdate;

    @Inject @BeforeDestroy
    private Event<T> beforeDestroy;

    @Inject @AfterDestroy
    private Event<T> afterDestroy;

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

    //<editor-fold defaultstate="collapsed" desc="Callbacks Delegators">
    @Override
    public Event<T> getBeforeSave() {
        return beforeSave;
    }

    @Override
    public Event<T> getAfterSave() {
        return afterSave;
    }

    @Override
    public Event<T> getBeforeCreate() {
        return beforeCreate;
    }

    @Override
    public Event<T> getAfterCreate() {
        return afterCreate;
    }

    @Override
    public Event<T> getBeforeUpdate() {
        return beforeUpdate;
    }

    @Override
    public Event<T> getAfterUpdate() {
        return afterUpdate;
    }

    @Override
    public Event<T> getBeforeDestroy() {
        return beforeDestroy;
    }

    @Override
    public Event<T> getAfterDestroy() {
        return afterDestroy;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Core Delegators">
    @Override
    public Relation<T> getRelation() {
        return Core.super.getRelation();
    }

    @Override
    public Relation<T> all() {
        return Scoping.super.all();
    }

    @Override
    public Relation<T> defaultScope() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Class getRealClass() {
        return getClass().getSuperclass();
    }

    public JpaAdapter<T> getConnection() {
        return new JpaAdapter(getEntityManager(), entityClass);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Private">
    private Class resolveEntityClass() {
        return (Class) Array.get(getParameterizedType().getActualTypeArguments(), 0);
    }

    private ParameterizedType getParameterizedType() {
        return (ParameterizedType) getRealClass().getGenericSuperclass();
    }
    //</editor-fold>

}
