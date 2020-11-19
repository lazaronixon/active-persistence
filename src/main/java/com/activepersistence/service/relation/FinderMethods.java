package com.activepersistence.service.relation;

import com.activepersistence.RecordNotFound;
import com.activepersistence.service.Arel;
import com.activepersistence.service.Relation;
import com.activepersistence.service.arel.SelectManager;
import com.activepersistence.service.arel.nodes.And;
import com.activepersistence.service.arel.nodes.JpqlLiteral;
import com.activepersistence.service.arel.visitors.Visitable;
import com.activepersistence.service.connectionadapters.JpaAdapter;
import static com.activepersistence.service.relation.ValueMethods.DISTINCT;
import static com.activepersistence.service.relation.ValueMethods.ORDER;
import static com.activepersistence.service.relation.ValueMethods.SELECT;
import static java.beans.Introspector.decapitalize;
import static java.lang.String.format;
import static java.util.Arrays.asList;
import java.util.List;
import static java.util.Optional.ofNullable;
import java.util.function.Function;
import static java.util.stream.Collectors.toList;

public interface FinderMethods<T> {

    public static final String ONE_AS_ONE = "1 AS one";

    public Class getEntityClass();

    public String getPrimaryKeyAttr();

    public String getAlias();

    public Values getValues();

    public SelectManager getArel();

    public JpaAdapter<T> getConnection();

    public boolean isLoaded();

    public T setTake(T take);

    public T getTake();

    public List<T> getRecords();

    public default T take() {
        return findTake();
    }

    public default List<T> take(int limit) {
        return findTakeWithLimit(limit);
    }

    public default T take$() {
        return ofNullable(take()).orElseGet(() -> raiseRecordNotFoundException());
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
        return getConnection().selectAll(constructRelationForExists().getArel()).size() == 1;
    }

    private Relation constructRelationForExists() {
        if (getValues().isDistinct() && getValues().getOffset() > 0) {
          return thiz().except(ORDER).limit(1);
        } else {
          return thiz().except(SELECT, DISTINCT, ORDER).selectScalar(ONE_AS_ONE).limit(1);
        }
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

    private T findTake() {
        if (isLoaded()) {
            return first(getRecords());
        } else {
            return ofNullable(getTake()).orElseGet(() -> setTake(first(thiz().limit(1).getRecords())));
        }
    }

    private List<T> findTakeWithLimit(int limit) {
       if (isLoaded()) {
            return limit(getRecords(), limit);
        } else {
            return thiz().limit(limit).getRecords();
        }
    }

    private T first(List<T> items) {
        return items.stream().findFirst().orElse(null);
    }

    private List<T> limit(List<T> items, int limit) {
        return items.stream().limit(limit).collect(toList());
    }

}
