package com.activepersistence.service;

import com.activepersistence.model.Base;
import java.util.function.Supplier;
import javax.enterprise.event.Event;
import javax.transaction.Transactional;

public interface Callbacks<T> extends Persistence<T> {

    public Event<T> getBeforeSave();

    public Event<T> getAfterSave();

    public Event<T> getBeforeCreate();

    public Event<T> getAfterCreate();

    public Event<T> getBeforeUpdate();

    public Event<T> getAfterUpdate();

    public Event<T> getBeforeDestroy();

    public Event<T> getAfterDestroy();

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
        getBeforeSave().fire(entity);
        var result = yield.get();
        getAfterSave().fire(result);

        return result;
    }

    private T _runCreateCallbacks(T entity, Supplier<T> yield) {
        getBeforeCreate().fire(entity);
        var result = yield.get();
        getAfterCreate().fire(result);

        return result;
    }

    private T _runUpdateCallbacks(T entity, Supplier<T> yield) {
        getBeforeUpdate().fire(entity);
        var result = yield.get();
        getAfterUpdate().fire(result);

        return result;
    }

    private void _runDestroyCallbacks(T entity, Runnable yield) {
        getBeforeDestroy().fire(entity);
        yield.run();
        getAfterDestroy().fire(entity);
    }

}
