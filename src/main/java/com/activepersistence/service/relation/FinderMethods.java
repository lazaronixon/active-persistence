package com.activepersistence.service.relation;

import com.activepersistence.service.Relation;
import static java.util.Arrays.asList;
import java.util.List;

public interface FinderMethods<T> {

    public String getPrimaryKey();

    public String getAlias();

    public Relation<T> thiz();

    public default T take() {
        return thiz().limit(1).fetchOne();
    }

    public default T take$() {
        return thiz().limit(1).fetchOne$();
    }

    public default List<T> take(int limit) {
        return thiz().limit(limit).fetch();
    }

    public default T first() {
        return thiz().order(getPrimaryKey()).take();
    }

    public default T first$() {
        return thiz().order(getPrimaryKey()).take$();
    }

    public default List<T> first(int limit) {
        return thiz().order(getPrimaryKey()).take(limit);
    }

    public default T last() {
        return thiz().order(getPrimaryKey() + " DESC").take();
    }

    public default T last$() {
        return thiz().order(getPrimaryKey() + " DESC").take$();
    }

    public default List<T> last(int limit) {
        return thiz().order(getPrimaryKey() + " DESC").take(limit);
    }

    public default T find(Object id) {
        return thiz().where(getPrimaryKey() + " = :pk").bind("pk", id).take$();
    }

    public default List<T> find(Object... ids) {
        return thiz().where(getPrimaryKey() + " IN :pks").bind("pks", asList(ids)).fetch();
    }

    public default T findById(Object id) {
        return thiz().where(getPrimaryKey() + " = :pk").bind("pk", id).take();
    }

    public default T findById$(Object id) {
        return thiz().where(getPrimaryKey() + " = :pk").bind("pk", id).take$();
    }

    public default boolean exists() {
        return thiz().limit(1).fetchExists();
    }
}
