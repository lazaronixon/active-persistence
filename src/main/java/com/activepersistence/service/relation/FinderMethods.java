package com.activepersistence.service.relation;

import com.activepersistence.service.Relation;
import java.util.List;

public interface FinderMethods<T> {

    public String getPrimaryKey();

    public Relation<T> thiz();

    public default T take() {
        return thiz().limit(1).fetchOne();
    }

    public default T takeOrFail() {
        return thiz().limit(1).fetchOneOrFail();
    }

    public default List<T> take(int limit) {
        return thiz().limit(limit).fetch();
    }

    public default T first() {
        return thiz().order(getPrimaryKey()).take();
    }

    public default T firstOrFail() {
        return thiz().order(getPrimaryKey()).takeOrFail();
    }

    public default List<T> first(int limit) {
        return thiz().order(getPrimaryKey()).take(limit);
    }

    public default T last() {
        return thiz().order(getPrimaryKey() + " DESC").take();
    }

    public default T lastOrFail() {
        return thiz().order(getPrimaryKey() + " DESC").takeOrFail();
    }

    public default List<T> last(int limit) {
        return thiz().order(getPrimaryKey() + " DESC").take(limit);
    }

    public default T find(Object id) {
        return thiz().where(getPrimaryKey() + " = :_id").bind("_id", id).takeOrFail();
    }

    public default List<T> find(List<Object> ids) {
        return thiz().where(getPrimaryKey() + " IN :_ids").bind("_ids", ids).fetch();
    }

    public default T findBy(String conditions) {
        return thiz().where(conditions).take();
    }

    public default T findByOrFail(String conditions, Object... params) {
        return thiz().where(conditions).takeOrFail();
    }

    public default boolean exists(String conditions) {
        return thiz().where(conditions).exists();
    }

    public default boolean exists() {
        return thiz().limit(1).fetchExists();
    }
}
