package com.activepersistence.repository;

import com.activepersistence.model.Base;
import java.util.function.Supplier;
import javax.transaction.Transactional;

public interface Callbacks<T> extends Persistence<T> {

    public default void beforeSave(T entity) {}

    public default void afterSave(T entity) {}

    public default void beforeCreate(T entity) {}

    public default void afterCreate(T entity) {}

    public default void beforeUpdate(T entity) {}

    public default void afterUpdate(T entity) {}

    public default void beforeDestroy(T entity) {}

    public default void afterDestroy(T entity) {}

    @Override @Transactional
    public default T save(Base entity) {
        return _runSaveCallbacks((T) entity,() -> Persistence.super.save(entity));
    }

    @Override @Transactional
    public default void destroy(Base entity) {
        _runDestroyCallbacks((T) entity,() -> Persistence.super.destroy(entity));
    }

    @Override
    public default T _createRecord(T entity) {
        return _runCreateCallbacks(entity,() -> Persistence.super._createRecord(entity));
    }

    @Override
    public default T _updateRecord(T entity) {
        return _runUpdateCallbacks(entity,() -> Persistence.super._updateRecord(entity));
    }

    private T _runSaveCallbacks(T entity, Supplier<T> yield) {
        beforeSave(entity); var result = yield.get(); afterSave(result); return result;
    }

    private T _runCreateCallbacks(T entity, Supplier<T> yield) {
        beforeCreate(entity); var result = yield.get(); afterCreate(result); return result;
    }

    private T _runUpdateCallbacks(T entity, Supplier<T> yield) {
        beforeUpdate(entity); var result = yield.get(); afterUpdate(result); return result;
    }

    private void _runDestroyCallbacks(T entity, Runnable yield) {
        beforeDestroy(entity); yield.run(); afterDestroy(entity);
    }

}
