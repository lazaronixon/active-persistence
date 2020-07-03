package com.activepersistence.service;

import java.util.function.Supplier;
import javax.persistence.EntityManager;
import javax.transaction.Transactional;

public interface Persistence<T> {

    public EntityManager getEntityManager();

    @Transactional
    public default T create(T entity) {
        return flush(() -> { getEntityManager().persist(entity); return entity; });
    }

    @Transactional
    public default T update(T entity) {
        return flush(() -> getEntityManager().merge(entity));
    }

    @Transactional
    public default void destroy(T entity) {
        flush(() -> getEntityManager().remove(getEntityManager().merge(entity)));
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

    private T flush(Supplier<T> yield) {
        T result = yield.get(); getEntityManager().flush(); return result;
    }

    private void flush(Runnable yield) {
        yield.run(); getEntityManager().flush();
    }

}