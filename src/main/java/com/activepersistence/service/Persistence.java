package com.activepersistence.service;

import java.util.function.Supplier;
import javax.persistence.EntityManager;
import static javax.persistence.LockModeType.NONE;
import static javax.persistence.LockModeType.PESSIMISTIC_WRITE;
import javax.transaction.Transactional;

public interface Persistence<T> {

    public EntityManager getEntityManager();

    @Transactional
    public default T create(com.activepersistence.model.Base entity) {
        return flush(() -> { getEntityManager().persist(entity); return (T) entity; });
    }

    @Transactional
    public default T update(com.activepersistence.model.Base entity) {
        return flush(() -> (T) getEntityManager().merge(entity));
    }

    @Transactional
    public default T save(com.activepersistence.model.Base entity) {
        if (entity.isDestroyed()) return (T) entity;

        if (entity.isNewRecord()) {
            return create(entity);
        } else {
            return update(entity);
        }
    }

    @Transactional
    public default void destroy(com.activepersistence.model.Base entity) {
        flush(() -> { if (entity.isPersisted()) getEntityManager().remove(getEntityManager().merge(entity)); });
    }

    public default void reload(T entity) {
        reload(entity, false);
    }

    public default void reload(T entity, boolean lock) {
        getEntityManager().refresh(entity, lock ? PESSIMISTIC_WRITE : NONE);
    }

    private T flush(Supplier<T> yield) {
        var result = yield.get(); getEntityManager().flush(); return result;
    }

    private void flush(Runnable yield) {
        yield.run(); getEntityManager().flush();
    }

}