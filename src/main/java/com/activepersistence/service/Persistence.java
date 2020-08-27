package com.activepersistence.service;

import java.util.function.Supplier;
import javax.persistence.EntityManager;
import static javax.persistence.LockModeType.NONE;
import static javax.persistence.LockModeType.PESSIMISTIC_WRITE;
import javax.transaction.Transactional;

public interface Persistence<T, ID> {

    public EntityManager getEntityManager();

    @Transactional
    public default T save(com.activepersistence.model.Base entity) {
        if (entity.isDestroyed()) return (T) entity;

        if (entity.isNewRecord()) {
            return create((T) entity);
        } else {
            return update((T) entity);
        }
    }

    @Transactional
    public default void destroy(com.activepersistence.model.Base entity) {
        if (!entity.isPersisted()) return;

        flush(() -> {
            if (getEntityManager().contains(entity)) {
                getEntityManager().remove(entity);
            } else {
                getEntityManager().remove(getEntityManager().merge(entity));
            }
        });
    }

    public default void reload(T entity) {
        reload(entity, false);
    }

    public default void reload(T entity, boolean lock) {
        getEntityManager().refresh(entity, lock ? PESSIMISTIC_WRITE : NONE);
    }

    private T create(T entity) {
        return flush(() -> { getEntityManager().persist(entity); return entity; });
    }

    @Transactional
    private T update(T entity) {
        return flush(() -> getEntityManager().merge(entity));
    }

    private T flush(Supplier<T> yield) {
        var result = yield.get(); getEntityManager().flush(); return result;
    }

    private void flush(Runnable yield) {
        yield.run(); getEntityManager().flush();
    }

}