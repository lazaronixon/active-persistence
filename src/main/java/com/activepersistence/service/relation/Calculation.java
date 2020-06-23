package com.activepersistence.service.relation;

import com.activepersistence.service.Relation;
import static java.lang.String.join;
import java.util.List;

public interface Calculation<T> {

    public Relation<T> thiz();

    public Relation<T> spawn();

    public default long count() {
        return count(thiz().getEntityAlias());
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

    public default List ids() {
        return pluck(thiz().getEntityAlias() + ".id");
    }

    public default List pluck(String... fields) {
        Relation<T> relation = spawn();
        relation.setCalculation(selectValuePluck(fields, relation));
        return relation.fetch_();
    }

    private Object calculate(String operation, String field) {
        Relation<T> relation = spawn();
        relation.setCalculation(selectValueCalculate(operation, field, relation));
        return relation.fetchOne();
    }

    private String selectValueCalculate(String operation, String field, Relation<T> relation) {
        return operation + "(" + distinct(relation) + field + ")";
    }

    private String selectValuePluck(String[] fields, Relation<T> relation) {
        return distinct(relation) + separatedByComma(fields);
    }

    private String distinct(Relation<T> relation) {
        return relation.hasDistinct() ? "DISTINCT " : "";
    }

    private String separatedByComma(String[] values) {
        return join(", ", values);
    }

}
