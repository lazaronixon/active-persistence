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
        relation.setSelect("COUNT(" + field + ")"); relation.setCalculating(true); return relation.fetchOneAs(Long.class);
    }

    public <R> R minimum(String field, Class<R> resultClass) {
        relation.setSelect("MIN(" + field + ")"); relation.setCalculating(true); return relation.fetchOneAs(resultClass);
    }

    public <R> R maximum(String field, Class<R> resultClass) {
        relation.setSelect("MAX(" + field + ")"); relation.setCalculating(true); return relation.fetchOneAs(resultClass);
    }

    public <R> R average(String field, Class<R> resultClass) {
        relation.setSelect("AVG(" + field + ")"); relation.setCalculating(true); return relation.fetchOneAs(resultClass);
    }

    public <R> R sum(String field, Class<R> resultClass) {
        relation.setSelect("SUM(" + field + ")"); relation.setCalculating(true); return relation.fetchOneAs(resultClass);
    }

    public List pluck(String... fields) {
        relation.setSelect(separatedByComma(fields)); return relation.fetchAlt();
    }

    public List ids() {
        return pluck("this.id");
    }

    private String separatedByComma(String[] values) {
        return join(", ", values);
    }

}
