package com.activepersistence.service.relation;

import com.activepersistence.service.Relation;

public class QueryMethods<T> {

    private final Relation<T> relation;

    public QueryMethods(Relation relation) {
        this.relation = relation;
    }

    public Relation<T> all() {
        return relation;
    }

    public Relation<T> select(String... fields) {
        relation.addSelect(fields); return relation;
    }

    public Relation<T> joins(String... values) {
        relation.addJoins(values); return relation;
    }

    public Relation<T> leftJoins(String... values) {
        relation.addLeftJoins(values); return relation;
    }

    public Relation<T> where(String conditions, Object... params) {
        relation.addWhere(conditions); relation.addParams(params); return relation;
    }

    public Relation<T> group(String... fields) {
        relation.addGroup(fields); return relation;
    }

    public Relation<T> having(String conditions, Object... params) {
        relation.addHaving(conditions); relation.addParams(params); return relation;
    }

    public Relation<T> order(String... fields) {
        relation.addOrder(fields); return relation;
    }

    public Relation<T> limit(int limit) {
        relation.setLimit(limit); return relation;
    }

    public Relation<T> offset(int offset) {
        relation.setOffset(offset); return relation;
    }

    public Relation<T> distinct() {
        relation.setDistinct(true); return relation;
    }

    public Relation<T> none() {
        relation.addWhere("1 = 0"); return relation;
    }

    public Relation<T> includes(String... includes) {
        relation.addIncludes(includes); return relation;
    }

    public Relation<T> eagerLoads(String... eagerLoads) {
        relation.addEagerLoads(eagerLoads); return relation;
    }

    public Relation<T> reselect(String... fields) {
        relation.clearSelect(); return select(fields);
    }

    public Relation<T> rewhere(String conditions, Object... params) {
        relation.clearWhere(); return where(conditions, params);
    }

    public Relation<T> reorder(String... fields) {
        relation.clearOrder(); return order(fields);
    }

    public Relation<T> lock() {
        relation.setLock(true); return relation;
    }

    public Relation<T> from(String value) {
        relation.setFromClause(value); return relation;
    }
}
