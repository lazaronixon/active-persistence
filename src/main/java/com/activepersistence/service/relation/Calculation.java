package com.activepersistence.service.relation;

import static com.activepersistence.service.Arel.jpql;
import com.activepersistence.service.Relation;
import com.activepersistence.service.arel.nodes.Function;
import static com.activepersistence.service.relation.Calculation.Operations.*;
import static com.activepersistence.service.relation.ValueMethods.CONSTRUCTOR;
import static com.activepersistence.service.relation.ValueMethods.DISTINCT;
import static java.util.Arrays.asList;
import static java.util.Arrays.copyOfRange;
import java.util.List;
import static java.util.stream.Collectors.toMap;

public interface Calculation<T> {

    public enum Operations { COUNT, MIN, MAX, AVG, SUM }

    public Values getValues();

    public String getAlias();

    public String getPrimaryKey();

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
        return pluck(getPrimaryKey());
    }

    public default List pluck(String... fields) {
        Relation<T> relation = spawn();

        relation.getValues().setConstructor(false);
        relation.getValues().setSelect(asList(fields));
        return relation.fetch$();
    }

    private Object calculate(Operations operation, String field) {
        Relation relation = spawn().except(CONSTRUCTOR, DISTINCT);

        if (relation.getValues().getGroup().isEmpty()) {
            return executeSimpleCalculation(relation, operation, field);
        } else {
            return executeGroupedCalculation(relation, operation, field);
        }
    }

    private Object executeSimpleCalculation(Relation<T> relation, Operations operation, String field) {
        relation.getValues().setSelect(asList(operationOverAggregateColumn(operation, field).toJpql())); return relation.fetchOne();
    }

    private Object executeGroupedCalculation(Relation<T> relation, Operations operation, String field) {
        Values values = relation.getValues();
        values.getSelect().clear();
        values.getSelect().add(operationOverAggregateColumn(operation, field).toJpql());
        values.getSelect().addAll(values.getGroup());
        return fetchGroupedResult(relation, values);
    }

    private Function operationOverAggregateColumn(Operations operation, String field) {
        switch (operation) {
            case COUNT:
                return jpql(field).count(getValues().isDistinct());
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

    private Object fetchGroupedResult(Relation<T> relation, Values values) {
        List<Object[]> results = relation.fetch$();

        if (values.getGroup().size() > 1) {
            return results.stream().collect(toMap(v -> copyOfRange(v, 1, v.length), v -> v[0]));
        } else {
            return results.stream().collect(toMap(v -> v[1], v -> v[0]));
        }
    }

}
