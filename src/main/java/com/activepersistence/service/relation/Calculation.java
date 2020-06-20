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
        getRelation().setSelect("COUNT(" + distinct() + field + ")"); return (long) getRelation().fetchOne();
    }

    public default Object minimum(String field) {
        getRelation().setSelect("MIN(" + field + ")"); return getRelation().fetchOne();
    }

    public default Object maximum(String field) {
        getRelation().setSelect("MAX(" + field + ")"); return getRelation().fetchOne();
    }

    public default Object average(String field) {
        getRelation().setSelect("AVG(" + field + ")"); return getRelation().fetchOne();
    }

    public default Object sum(String field) {
        getRelation().setSelect("SUM(" + field + ")"); return getRelation().fetchOne();
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
