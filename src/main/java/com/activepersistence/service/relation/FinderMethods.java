package com.activepersistence.service.relation;

import com.activepersistence.service.Relation;
import static java.util.Arrays.asList;
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
        return thiz().where(getPrimaryKey() + " = ?", id).takeOrFail();
    }

    public default List<T> find(Object... ids) {
        return thiz().where(getPrimaryKey() + " IN (?)", asList(ids)).fetch();
    }

    public default T findById(Object id) {
        return findBy(getPrimaryKey() + " = ?", id);
    }

    public default T findByIdOrFail(Object id) {
        return findByOrFail(getPrimaryKey() + " = ?", id);
    }

    public default T findBy(String conditions, Object... params) {
        return thiz().where(conditions, params).take();
    }

    public default T findByOrFail(String conditions, Object... params) {
        return thiz().where(conditions, params).takeOrFail();
    }

    public default boolean exists(String conditions, Object... params) {
        return thiz().where(conditions, params).exists();
    }

    public default boolean exists() {
        return thiz().limit(1).fetchExists();
    }
}
