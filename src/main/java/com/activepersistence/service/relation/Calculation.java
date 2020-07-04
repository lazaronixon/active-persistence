package com.activepersistence.service.relation;

import static com.activepersistence.service.Arel.jpql;
import com.activepersistence.service.Relation;
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
        relation.getValues().setConstructor(false);
        relation.getValues().setDistinctValue(false);

        if (operation.equals("COUNT")) {
            relation.getValues().setSelectValues(Set.of(jpql(field).count(isDistinct()).toJpql()));
        } else if (operation.equals("MIN")) {
            relation.getValues().setSelectValues(Set.of(jpql(field).minimum().toJpql()));
        } else if (operation.equals("MAX")) {
            relation.getValues().setSelectValues(Set.of(jpql(field).maximum().toJpql()));
        } else if (operation.equals("AVG")) {
            relation.getValues().setSelectValues(Set.of(jpql(field).average().toJpql()));
        } else if (operation.equals("SUM")) {
            relation.getValues().setSelectValues(Set.of(jpql(field).sum().toJpql()));
        } else {
            throw new RuntimeException("Operation not supported: " + operation);
        }

        return relation.fetchOne();
    }

    private boolean isDistinct() {
        return thiz().getValues().isDistinctValue();
    }

}
