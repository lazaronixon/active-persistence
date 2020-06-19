package com.activepersistence.service.relation;

import com.activepersistence.service.Relation;
import static java.lang.String.join;
import java.util.List;

public interface Calculation<T> {

    public Relation<T> getRelation();

    public default long count() {
        return count("this");
    }

    public default long count(String field) {
        getRelation().setSelect("COUNT(" + distinct() + field + ")"); return getRelation().fetchOneAs(Long.class);
    }

    public default <R> R minimum(String field, Class<R> resultClass) {
        getRelation().setSelect("MIN(" + field + ")"); return getRelation().fetchOneAs(resultClass);
    }

    public default <R> R maximum(String field, Class<R> resultClass) {
        getRelation().setSelect("MAX(" + field + ")"); return getRelation().fetchOneAs(resultClass);
    }

    public default <R> R average(String field, Class<R> resultClass) {
        getRelation().setSelect("AVG(" + field + ")"); return getRelation().fetchOneAs(resultClass);
    }

    public default <R> R sum(String field, Class<R> resultClass) {
        getRelation().setSelect("SUM(" + field + ")"); return getRelation().fetchOneAs(resultClass);
    }

    public default List pluck(String... fields) {
        getRelation().setSelect(distinct() + separatedByComma(fields)); return getRelation().fetchAlt();
    }

    public default List ids() {
        return pluck("this.id");
    }

    private String separatedByComma(String[] values) {
        return join(", ", values);
    }

    private String distinct() {
        return getRelation().hasDistinct() ? "DISTINCT " : "";
    }

}
