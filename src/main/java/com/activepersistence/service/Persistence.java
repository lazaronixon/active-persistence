package com.activepersistence.service;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;

public interface Persistence<T> {

    public EntityManager getEntityManager();

    @Transactional
    public default T create(T entity) {
        getEntityManager().persist(entity); return entity;
    }

    @Transactional
    public default T update(T entity) {
        return getEntityManager().merge(entity);
    }

    @Transactional
    public default void destroy(T entity) {
        getEntityManager().remove(getEntityManager().merge(entity));
    }

    @Transactional
    public default T save(com.activepersistence.model.Base entity) {
        if (entity.isNewRecord()) {
            return create((T) entity);
        } else {
            return update((T) entity);
        }
    }

    public default void reload(T entity) {
        getEntityManager().refresh(entity);
    }

}