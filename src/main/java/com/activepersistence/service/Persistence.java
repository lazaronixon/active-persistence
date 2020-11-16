package com.activepersistence.service;

import com.activepersistence.model.Base;
import javax.persistence.EntityManager;
import static javax.persistence.LockModeType.NONE;
import static javax.persistence.LockModeType.PESSIMISTIC_WRITE;

public interface Persistence<T> {

    public EntityManager getEntityManager();

    public default T save(Base entity) {
        return createOrUpdate(entity);
    }

    public default void destroy(Base entity) {
        if (!entity.isPersisted()) return;

        if (getEntityManager().contains(entity)) {
            getEntityManager().remove(entity);
            getEntityManager().flush();
        } else {
            getEntityManager().remove(getEntityManager().merge(entity));
            getEntityManager().flush();
        }
    }

    public default void reload(T entity) {
        reload(entity, false);
    }

    public default void reload(T entity, boolean lock) {
        getEntityManager().refresh(entity, lock ? PESSIMISTIC_WRITE : NONE);
    }

    public default T _createRecord(T entity) {
        getEntityManager().persist(entity); getEntityManager().flush(); return entity;
    }

    public default T _updateRecord(T entity) {
        var merged = getEntityManager().merge(entity); getEntityManager().flush(); return merged;
    }

    private T createOrUpdate(Base entity) {
        if (entity.isDestroyed()) return (T) entity;

        if (entity.isNewRecord()) {
            return _createRecord((T) entity);
        } else {
            return _updateRecord((T) entity);
        }
    }

}