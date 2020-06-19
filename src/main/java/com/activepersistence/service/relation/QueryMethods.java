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
        getRelation().addWhere(conditions, params); return getRelation();
    }

    public default Relation<T> group(String... fields) {
        getRelation().addGroup(fields); return getRelation();
    }

    public default Relation<T> having(String conditions, Object... params) {
        getRelation().addHaving(conditions, params); return getRelation();
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

    public default Relation<T> unscope(ValidUnscopingValues... values) {
        for (ValidUnscopingValues value : values) {
            switch (value) {
                case SELECT:
                    getRelation().clearSelect();
                case FROM:
                    getRelation().clearFrom();
                case JOINS:
                    getRelation().clearJoins();
                case WHERE:
                    getRelation().clearWhere();
                case GROUP:
                    getRelation().clearGroup();
                case HAVING:
                    getRelation().clearHaving();
                case ORDER:
                    getRelation().clearOrder();
                case LIMIT:
                    getRelation().limit(0);
                case OFFSET:
                    getRelation().offset(0);
                case INCLUDES:
                    getRelation().clearIncludes();
                case EAGER_LOADS:
                    getRelation().clearEagerLoads();
                case LOCK:
                    getRelation().setLock(false);
            }
        }
        return getRelation();
    }

    public default Relation<T> reselect(String... fields) {
        return getRelation().unscope(ValidUnscopingValues.SELECT).select(fields);
    }

    public default Relation<T> rewhere(String conditions, Object... params) {
        return getRelation().unscope(ValidUnscopingValues.WHERE).where(conditions, params);
    }

    public default Relation<T> reorder(String... fields) {
        return getRelation().unscope(ValidUnscopingValues.ORDER).order(fields);
    }

    public default Relation<T> lock() {
        getRelation().setLock(true); return getRelation();
    }

    public default Relation<T> from(String value) {
        getRelation().setFromClause(value); return getRelation();
    }
}
