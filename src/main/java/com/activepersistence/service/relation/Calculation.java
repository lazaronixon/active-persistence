package com.activepersistence.service.relation;

import static com.activepersistence.service.Arel.jpql;
import com.activepersistence.service.Relation;
import com.activepersistence.service.arel.nodes.Function;
import static java.util.Arrays.asList;
import static java.util.Arrays.copyOfRange;
import java.util.List;
import static java.util.stream.Collectors.toMap;

public interface Calculation<T> {

    public Relation<T> spawn();

    public Values getValues();

    public String getAlias();

    public String getPrimaryKey();

    public default Object count() {
        return count(getAlias());
    }

    public default Object count(String field) {
        return calculate("COUNT", field);
    }

    public default Object minimum(String field) {
        return calculate("MIN", field);
    }

    public default Object maximum(String field) {
        return calculate("MAX", field);
    }

    public default Object average(String field) {
        return calculate("AVG", field);
    }

    public default Object sum(String field) {
        return calculate("SUM", field);
    }

    public default List<Object> ids() {
        return pluck(getPrimaryKey());
    }

    public default List pluck(String... fields) {
        Relation<T> relation = spawn();
        relation.getValues().setConstructor(false);
        relation.getValues().setSelectValues(asList(fields));
        return relation.fetch_();
    }

    private Object calculate(String operation, String field) {
        Relation relation = spawn();
        relation.getValues().setConstructor(false);
        relation.getValues().setDistinctValue(false);

        if (relation.getValues().getGroupValues().isEmpty()) {
            return executeSimpleCalculation(relation, operation, field);
        } else {
            return executeGroupedCalculation(relation, operation, field);
        }
    }

    private Object executeSimpleCalculation(Relation<T> relation, String operation, String field) {
        relation.getValues().setSelectValues(asList(operationOverAggregateColumn(operation, field).toJpql())); return relation.fetchOne();
    }

    private Object executeGroupedCalculation(Relation<T> relation, String operation, String field) {
        Values values = relation.getValues();
        values.getSelectValues().clear();
        values.getSelectValues().add(operationOverAggregateColumn(operation, field).toJpql());
        values.getSelectValues().addAll(values.getGroupValues());
        return fetchGroupedResult(relation, values);
    }

    private Function operationOverAggregateColumn(String operation, String field) {
        switch (operation) {
            case "COUNT":
                return jpql(field).count(isDistinct());
            case "MIN":
                return jpql(field).minimum();
            case "MAX":
                return jpql(field).maximum();
            case "AVG":
                return jpql(field).average();
            case "SUM":
                return jpql(field).sum();
            default:
                throw new RuntimeException("Operation not supported: " + operation);
        }
    }

    private Object fetchGroupedResult(Relation<T> relation, Values values) {
        List<Object[]> results = relation.fetch_();

        if (values.getGroupValues().size() > 1) {
            return results.stream().collect(toMap(v -> copyOfRange(v, 1, v.length), v -> v[0]));
        } else {
            return results.stream().collect(toMap(v -> v[1], v -> v[0]));
        }
    }

    private boolean isDistinct() {
        return getValues().isDistinctValue();
    }

}
