package com.activepersistence.service.relation;

import static com.activepersistence.service.Arel.jpql;
import com.activepersistence.service.Relation;
import static java.util.Arrays.asList;
import java.util.List;
import java.util.Set;

public interface Calculation<T> {

    public Relation<T> thiz();

    public Relation<T> spawn();

    public default Object count() {
        return count("this");
    }

    public default Object count(String field) {
        return (long) calculate("COUNT", field);
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
        return pluck("this.id");
    }

    public default List<Object> pluck(String... fields) {
        Relation relation = thiz().spawn();
        relation.getValues().setConstructor(false);
        relation.getValues().setSelectValues(Set.of(fields));
        return relation.fetch_();
    }

    private Object calculate(String operation, String field) {
        Relation relation = thiz().spawn();
        Values   values   = relation.getValues();
        values.setConstructor(false);
        values.setDistinctValue(false);

        if (operation.equals("COUNT")) {
            values.setSelectValues(Set.of(jpql(field).count(isDistinct()).toJpql()));
        } else if (operation.equals("MIN")) {
            values.setSelectValues(Set.of(jpql(field).minimum().toJpql()));
        } else if (operation.equals("MAX")) {
            values.setSelectValues(Set.of(jpql(field).maximum().toJpql()));
        } else if (operation.equals("AVG")) {
            values.setSelectValues(Set.of(jpql(field).average().toJpql()));
        } else if (operation.equals("SUM")) {
            values.setSelectValues(Set.of(jpql(field).sum().toJpql()));
        } else {
            throw new RuntimeException("Operation not supported: " + operation);
        }

        return relation.fetchOne();
    }

    private boolean isDistinct() {
        return thiz().getValues().isDistinctValue();
    }

}
