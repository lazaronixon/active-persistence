package com.activepersistence.service.relation;

import com.activepersistence.service.Relation;
import static java.lang.Character.toLowerCase;
import static java.util.Arrays.asList;
import java.util.List;
import java.util.function.Function;
import static java.util.stream.Collectors.joining;

public interface FinderMethods<T> {

    public String getPrimaryKey();

    public String getAlias();

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

    public default T findByExp(String expression, Object... params) {
        return findBy(exprToJpql(expression), params);
    }

    public default T findByExpOrFail(String expression, Object... params) {
        return findByOrFail(exprToJpql(expression), params);
    }

    public default boolean exists(String conditions, Object... params) {
        return thiz().where(conditions, params).exists();
    }

    public default boolean exists() {
        return thiz().limit(1).fetchExists();
    }

    private String exprToJpql(String expression) {
        return splitExpr(expression).stream().map(parametize()).collect(joining(" AND "));
    }

    private Function<String, String> parametize() {
        return attr -> (getAlias() + "." + uncapitalize(attr) + " = ?");
    }

    private List<String> splitExpr(String expression) {
        return asList(expression.split("And"));
    }

    private static String uncapitalize(String word) {
        return toLowerCase(word.charAt(0)) + word.substring(1);
    }
}
