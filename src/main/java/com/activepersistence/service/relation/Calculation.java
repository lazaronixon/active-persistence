package com.activepersistence.service.relation;

import static com.activepersistence.service.Arel.jpql;
import com.activepersistence.service.Relation;
import com.activepersistence.service.arel.SelectManager;
import com.activepersistence.service.arel.nodes.Function;
import com.activepersistence.service.connectionadapters.JpaAdapter;
import static com.activepersistence.service.relation.Calculation.Operations.*;
import static com.activepersistence.service.relation.ValueMethods.ORDER;
import java.util.ArrayList;
import static java.util.Arrays.asList;
import static java.util.Arrays.copyOfRange;
import java.util.List;
import static java.util.stream.Collectors.toMap;
import javax.transaction.NotSupportedException;

public interface Calculation<T> {

    public enum Operations { COUNT, MIN, MAX, AVG, SUM }

    public Values getValues();

    public String getAlias();

    public boolean hasLimitOrOffset();

    public String getPrimaryKeyAttr();

    public JpaAdapter<T> getConnection();

    public Relation<T> spawn();

    public default Object count() {
        return count(getAlias());
    }

    public default Object count(String field) {
        return calculate(COUNT, field);
    }

    public default Object minimum(String field) {
        return calculate(MIN, field);
    }

    public default Object maximum(String field) {
        return calculate(MAX, field);
    }

    public default Object average(String field) {
        return calculate(AVG, field);
    }

    public default Object sum(String field) {
        return calculate(SUM, field);
    }

    public default List<Object> ids() {
        return pluck(getPrimaryKeyAttr());
    }

    public default List pluck(String... fields) {
        var relation = spawn();
        relation.getValues().setConstructor(false);
        relation.getValues().setSelect(asList(fields));

        return getConnection().selectAll(relation.getArel());
    }

    public default Object calculate(Operations operation, String field) {
        var distinct = getValues().isDistinct();

        if (getValues().getGroup().isEmpty()) {
            return executeSimpleCalculation(operation, field, distinct);
        } else {
            return executeGroupedCalculation(operation, field, distinct);
        }
    }

    private Object executeSimpleCalculation(Operations operation, String field, boolean distinct) {
        SelectManager queryBuilder = null;

        if (operation == COUNT && hasLimitOrOffset()) {
            throw new UnsupportedOperationException();
        } else {
            var relation = thiz().unscope(ORDER).distinct(false);

            var selectValue = operationOverAggregateColumn(operation, field, distinct);
            relation.getValues().setConstructor(false);
            relation.getValues().setSelect(asList(selectValue.toJpql()));

            queryBuilder = relation.getArel();
        }

        return getConnection().selectAll(queryBuilder).stream().findFirst().orElse(null);
    }

    private Object executeGroupedCalculation(Operations operation, String field, boolean distinct) {
        var groupFields = getValues().getGroup();

        var selectValue = operationOverAggregateColumn(operation, field, distinct);

        var selectValues = new ArrayList();
        selectValues.add(selectValue.toJpql());
        selectValues.addAll(getValues().getSelect());

        var relation = thiz().distinct(false);
        relation.getValues().setConstructor(false);
        relation.getValues().setSelect(selectValues);
        relation.getValues().getSelect().addAll(groupFields);

        List<Object[]> calculatedData = getConnection().selectAll(relation.getArel());

        if (groupFields.size() > 1) {
            return calculatedData.stream().collect(toMap(v -> copyOfRange(v, 1, v.length), v -> v[0]));
        } else {
            return calculatedData.stream().collect(toMap(v -> v[1], v -> v[0]));
        }
    }

    private Function operationOverAggregateColumn(Operations operation, String field, boolean distinct) {
        switch (operation) {
            case COUNT:
                return jpql(field).count(distinct);
            case MIN:
                return jpql(field).minimum();
            case MAX:
                return jpql(field).maximum();
            case AVG:
                return jpql(field).average();
            case SUM:
                return jpql(field).sum();
            default:
                throw new RuntimeException("Operation not supported: " + operation);
        }
    }

    private Relation<T> thiz() {
        return (Relation<T>) this;
    }

}
