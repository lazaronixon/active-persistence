package com.activepersistence.service.relation;

import com.activepersistence.RecordNotFound;
import com.activepersistence.service.Arel;
import com.activepersistence.service.Relation;
import com.activepersistence.service.arel.SelectManager;
import com.activepersistence.service.arel.nodes.And;
import com.activepersistence.service.arel.nodes.JpqlLiteral;
import com.activepersistence.service.arel.visitors.Visitable;
import static java.beans.Introspector.decapitalize;
import static java.lang.String.format;
import static java.util.Arrays.asList;
import java.util.List;
import static java.util.Optional.ofNullable;
import java.util.function.Function;
import static java.util.stream.Collectors.toList;

public interface FinderMethods<T> {

    public Class getEntityClass();

    public String getPrimaryKeyAttr();

    public String getAlias();

    public Values getValues();

    public SelectManager getArel();

    public default T take() {
        return thiz().limit(1).getRecords().stream().findFirst().orElse(null);
    }

    public default T take$() {
        return ofNullable(take()).orElseGet(() -> raiseRecordNotFoundException());
    }

    public default List<T> take(int limit) {
        return thiz().limit(limit).getRecords();
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
        return thiz().where(getPrimaryKeyAttr() + " IN (?)", asList(ids)).getRecords();
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
        return findBy(expressionToJpql(expression), params);
    }

    public default T findByExpression$(String expression, Object... params) {
        return findBy$(expressionToJpql(expression), params);
    }

    public default boolean exists(String conditions, Object... params) {
        return thiz().where(conditions, params).exists();
    }

    public default boolean exists() {
        return thiz().limit(1).getRecords().size() == 1;
    }

    private T raiseRecordNotFoundException() {
        if (getValues().getWhere().isEmpty()) {
            throw new RecordNotFound(format("Couldn't find %s", className()));
        } else {
            throw new RecordNotFound(format("Couldn't find %s with %s", className(), getConditions()));
        }
    }

    private String expressionToJpql(String expression) {
        return new And(conditionsFor(expression)).toJpql();
    }

    private List<Visitable> conditionsFor(String expression) {
        return asList(expression.split("And")).stream().map(toConditions()).map(toLiteral()).collect(toList());
    }

    private Function<String, String> toConditions() {
        return attr -> getAlias() + "." + decapitalize(attr) + " = ?";
    }

    private Function<String, JpqlLiteral> toLiteral() {
        return attr -> Arel.jpql(attr);
    }

    private String className() {
        return getEntityClass().getSimpleName();
    }

    private String getConditions() {
        return "[" + getArel().whereJpql() + "]";
    }

    private Relation<T> thiz() {
        return (Relation<T>) this;
    }
}
