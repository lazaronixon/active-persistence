package com.activepersistence.service.relation;

import com.activepersistence.service.Relation;
import static java.lang.String.join;
import java.util.List;

public class Calculation<T> {

    private final Relation<T> relation;

    public Calculation(Relation relation) {
        this.relation = relation;
    }

    public long count() {
        return count("this");
    }

    public long count(String field) {
        relation.setSelect("COUNT(" + distinct() + field + ")"); return relation.fetchOneAs(Long.class);
    }

    public <R> R minimum(String field, Class<R> resultClass) {
        relation.setSelect("MIN(" + field + ")"); return relation.fetchOneAs(resultClass);
    }

    public <R> R maximum(String field, Class<R> resultClass) {
        relation.setSelect("MAX(" + field + ")"); return relation.fetchOneAs(resultClass);
    }

    public <R> R average(String field, Class<R> resultClass) {
        relation.setSelect("AVG(" + field + ")"); return relation.fetchOneAs(resultClass);
    }

    public <R> R sum(String field, Class<R> resultClass) {
        relation.setSelect("SUM(" + field + ")"); return relation.fetchOneAs(resultClass);
    }

    public List pluck(String... fields) {
        relation.setSelect(distinct() + separatedByComma(fields)); return relation.fetchAlt();
    }

    public List ids() {
        return pluck("this.id");
    }

    private String separatedByComma(String[] values) {
        return join(", ", values);
    }

    private String distinct() {
        return relation.isDistinct() ? "DISTINCT " : "";
    }

}
