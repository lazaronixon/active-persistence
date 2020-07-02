package com.activepersistence.service.relation;

import static com.activepersistence.service.Arel.jpql;
import com.activepersistence.service.Relation;
import static java.util.Arrays.asList;
import java.util.List;

public interface Calculation<T> {

    public Relation<T> thiz();

    public Relation<T> spawn();

    public default long count() {
        return count("this");
    }

    public default long count(String field) {
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
        relation.getValues().setConstructor(null);
        relation.getValues().getSelectValues().clear();
        relation.getValues().getSelectValues().addAll(asList(fields));
        return relation.fetch_();
    }

    private Object calculate(String operation, String field) {
        Relation relation = thiz().spawn();
        relation.getValues().setConstructor(null);
        relation.getValues().setDistinctValue(false);
        relation.getValues().getSelectValues().clear();

        boolean distinct = thiz().getValues().isDistinctValue();

        if(operation.equals("COUNT")) {
            relation.getValues().getSelectValues().add(jpql(field).count(distinct).toJpql());
        } else if (operation.equals("MIN")) {
            relation.getValues().getSelectValues().add(jpql(field).minimum().toJpql());
        } else if (operation.equals("MAX")) {
            relation.getValues().getSelectValues().add(jpql(field).maximum().toJpql());
        } else if (operation.equals("AVG")) {
            relation.getValues().getSelectValues().add(jpql(field).average().toJpql());
        } else if (operation.equals("SUM")) {
            relation.getValues().getSelectValues().add(jpql(field).sum().toJpql());
        } else {
            throw new RuntimeException("Operation not supported: " + operation);
        }

        return relation.fetchOne();
    }

}
