package com.activepersistence.service.relation;

import com.activepersistence.service.Relation;

public interface QueryMethods<T> {

    public Relation<T> spawn();

    public default Relation<T> all() {
        return spawn();
    }

    public default Relation<T> select(String... fields) {
        return spawn().select_(fields);
    }

    public default Relation<T> select_(String... fields) {
        thiz().addSelect(fields); return thiz();
    }

    public default Relation<T> joins(String... values) {
        return spawn().joins_(values);
    }

    public default Relation<T> joins_(String... values) {
        thiz().addJoins(values); return thiz();
    }

    public default Relation<T> where(String conditions, Object... params) {
        return where_(conditions, params);
    }

    public default Relation<T> where_(String conditions, Object... params) {
        thiz().addWhere(conditions, params); return thiz();
    }

    public default Relation<T> group(String... fields) {
        return spawn().group_(fields);
    }

    public default Relation<T> group_(String... fields) {
        thiz().addGroup(fields); return thiz();
    }

    public default Relation<T> having(String conditions, Object... params) {
        return spawn().having_(conditions, params);
    }

    public default Relation<T> having_(String conditions, Object... params) {
        thiz().addHaving(conditions, params); return thiz();
    }

    public default Relation<T> order(String... fields) {
        return spawn().order_(fields);
    }

    public default Relation<T> order_(String... fields) {
        thiz().addOrder(fields); return thiz();
    }

    public default Relation<T> limit(int limit) {
        return spawn().limit_(limit);
    }

    public default Relation<T> limit_(int limit) {
        thiz().setLimit(limit); return thiz();
    }

    public default Relation<T> offset(int offset) {
        return spawn().offset_(offset);
    }

    public default Relation<T> offset_(int offset) {
        thiz().setOffset(offset); return thiz();
    }

    public default Relation<T> distinct() {
        return spawn().distinct_();
    }

    public default Relation<T> distinct_() {
        thiz().setDistinct(true); return thiz();
    }

    public default Relation<T> none() {
        return spawn().none_();
    }

    public default Relation<T> none_() {
        thiz().addWhere("1 = 0"); return thiz();
    }

    public default Relation<T> includes(String... includes) {
        return spawn().includes_(includes);
    }

    public default Relation<T> includes_(String... includes) {
        thiz().addIncludes(includes); return thiz();
    }

    public default Relation<T> eagerLoads(String... eagerLoads) {
        return spawn().eagerLoads_(eagerLoads);
    }

    public default Relation<T> eagerLoads_(String... eagerLoads) {
        thiz().addEagerLoads(eagerLoads); return thiz();
    }

    public default Relation<T> lock() {
        return this.lock_();
    }

    public default Relation<T> lock_() {
        thiz().setLock(true); return thiz();
    }

    public default Relation<T> from(String value) {
        return this.from_(value);
    }

    public default Relation<T> from_(String value) {
        thiz().setFromClause(value); return thiz();
    }

    public default Relation<T> unscope(ValidUnscopingValues... values) {
        this.unscope_(values); return (Relation<T>) this;
    }

    public default Relation<T> unscope_(ValidUnscopingValues... values) {
        for (ValidUnscopingValues value : values) {
            switch (value) {
                case SELECT:
                    thiz().clearSelect();
                case FROM:
                    thiz().clearFrom();
                case JOINS:
                    thiz().clearJoins();
                case WHERE:
                    thiz().clearWhere();
                case GROUP:
                    thiz().clearGroup();
                case HAVING:
                    thiz().clearHaving();
                case ORDER:
                    thiz().clearOrder();
                case LIMIT:
                    thiz().limit(0);
                case OFFSET:
                    thiz().offset(0);
                case INCLUDES:
                    thiz().clearIncludes();
                case EAGER_LOADS:
                    thiz().clearEagerLoads();
                case LOCK:
                    thiz().setLock(false);
            }
        }
        return (Relation<T>) this;
    }

    public default Relation<T> reselect(String... fields) {
        return spawn().unscope(ValidUnscopingValues.SELECT).select(fields);
    }

    public default Relation<T> rewhere(String conditions, Object... params) {
        return spawn().unscope(ValidUnscopingValues.WHERE).where(conditions, params);
    }

    public default Relation<T> reorder(String... fields) {
        return spawn().unscope(ValidUnscopingValues.ORDER).order(fields);
    }

    private Relation<T> thiz() {
        return (Relation<T>) this;
    }
}
