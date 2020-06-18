package com.activepersistence.service.relation;

import com.activepersistence.service.Relation;

public interface QueryMethods<T> {

    public Relation<T> getRelation();

    public default Relation<T> all() {
        return getRelation();
    }

    public default Relation<T> select(String... fields) {
        getRelation().addSelect(fields); return getRelation();
    }

    public default Relation<T> joins(String... values) {
        getRelation().addJoins(values); return getRelation();
    }

    public default Relation<T> where(String conditions, Object... params) {
        getRelation().addWhere(conditions); getRelation().addParams(params); return getRelation();
    }

    public default Relation<T> group(String... fields) {
        getRelation().addGroup(fields); return getRelation();
    }

    public default Relation<T> having(String conditions, Object... params) {
        getRelation().addHaving(conditions); getRelation().addParams(params); return getRelation();
    }

    public default Relation<T> order(String... fields) {
        getRelation().addOrder(fields); return getRelation();
    }

    public default Relation<T> limit(int limit) {
        getRelation().setLimit(limit); return getRelation();
    }

    public default Relation<T> offset(int offset) {
        getRelation().setOffset(offset); return getRelation();
    }

    public default Relation<T> distinct() {
        getRelation().setDistinct(true); return getRelation();
    }

    public default Relation<T> none() {
        getRelation().addWhere("1 = 0"); return getRelation();
    }

    public default Relation<T> includes(String... includes) {
        getRelation().addIncludes(includes); return getRelation();
    }

    public default Relation<T> eagerLoads(String... eagerLoads) {
        getRelation().addEagerLoads(eagerLoads); return getRelation();
    }

    public default Relation<T> reselect(String... fields) {
        getRelation().clearSelect(); return select(fields);
    }

    public default Relation<T> rewhere(String conditions, Object... params) {
        getRelation().clearWhere(); return where(conditions, params);
    }

    public default Relation<T> reorder(String... fields) {
        getRelation().clearOrder(); return order(fields);
    }

    public default Relation<T> lock() {
        getRelation().setLock(true); return getRelation();
    }

    public default Relation<T> from(String value) {
        getRelation().setFromClause(value); return getRelation();
    }
}
