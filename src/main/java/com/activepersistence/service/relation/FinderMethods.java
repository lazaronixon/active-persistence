package com.activepersistence.service.relation;

import com.activepersistence.service.Relation;
import static java.beans.Introspector.decapitalize;
import static java.util.Arrays.asList;
import java.util.List;
import java.util.function.Function;
import static java.util.stream.Collectors.joining;

public interface FinderMethods<T> {

    public String getPrimaryKeyAttr();

    public String getAlias();

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
        return thiz().order(getPrimaryKeyAttr()).take();
    }

    public default T first$() {
        return thiz().order(getPrimaryKeyAttr()).take$();
    }

    public default List<T> first(int limit) {
        return thiz().order(getPrimaryKeyAttr()).take(limit);
    }

    public default T last() {
        return thiz().order(getPrimaryKeyAttr() + " DESC").take();
    }

    public default T last$() {
        return thiz().order(getPrimaryKeyAttr() + " DESC").take$();
    }

    public default List<T> last(int limit) {
        return thiz().order(getPrimaryKeyAttr() + " DESC").take(limit);
    }

    public default T find(Object id) {
        return thiz().where(getPrimaryKeyAttr() + " = ?", id).take$();
    }

    public default List<T> find(Object... ids) {
        return thiz().where(getPrimaryKeyAttr() + " IN (?)", asList(ids)).fetch();
    }

    public default T findById(Object id) {
        return findBy(getPrimaryKeyAttr() + " = ?", id);
    }

    public default T findById$(Object id) {
        return findBy$(getPrimaryKeyAttr() + " = ?", id);
    }

    public default T findBy(String conditions, Object... params) {
        return thiz().where(conditions, params).take();
    }

    public default T findBy$(String conditions, Object... params) {
        return thiz().where(conditions, params).take$();
    }

    public default T findByExpression(String expression, Object... params) {
        return findBy(exprToJpql(expression), params);
    }

    public default T findByExpression$(String expression, Object... params) {
        return findBy$(exprToJpql(expression), params);
    }

    public default boolean exists(String conditions, Object... params) {
        return thiz().where(conditions, params).exists();
    }

    public default boolean exists() {
        return thiz().limit(1).fetchExists();
    }

    private String exprToJpql(String expression) {
        return asList(expression.split("And")).stream().map(parametize()).collect(joining(" AND "));
    }

    private Function<String, String> parametize() {
        return attr -> (getAlias() + "." + decapitalize(attr) + " = ?");
    }

    private Relation<T> thiz() {
        return (Relation<T>) this;
    }
}
