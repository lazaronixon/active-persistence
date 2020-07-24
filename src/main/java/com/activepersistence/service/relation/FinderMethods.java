package com.activepersistence.service.relation;

import com.activepersistence.service.Relation;
import static java.beans.Introspector.decapitalize;
import static java.util.Arrays.asList;
import java.util.List;
import static java.util.stream.Collectors.joining;

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
        return thiz().where(getPrimaryKey() + " = ?", id).take$();
    }

    public default List<T> find(Object... ids) {
        return thiz().where(getPrimaryKey() + " IN (?)", asList(ids)).fetch();
    }

    public default T findById(Object id) {
        return findBy(getPrimaryKey() + " = ?", id);
    }

    public default T findById$(Object id) {
        return findBy$(getPrimaryKey() + " = ?", id);
    }

    public default T findBy(String conditions, Object... params) {
        return thiz().where(conditions, params).take();
    }

    public default T findBy$(String conditions, Object... params) {
        return thiz().where(conditions, params).take$();
    }

    public default T findByExp(String expression, Object... params) {
        return findBy(exprToJpql(expression), params);
    }

    public default T findByExp$(String expression, Object... params) {
        return findBy$(exprToJpql(expression), params);
    }

    public default boolean exists(String conditions, Object... params) {
        return thiz().where(conditions, params).exists();
    }

    public default boolean exists() {
        return thiz().limit(1).fetchExists();
    }

    private String exprToJpql(String expression) {
        return asList(expression.split("And")).stream().map(attr -> (getAlias() + "." + decapitalize(attr) + " = ?")).collect(joining(" AND "));
    }
}
