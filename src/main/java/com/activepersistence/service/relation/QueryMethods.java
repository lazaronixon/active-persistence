package com.activepersistence.service.relation;

import com.activepersistence.service.Base;
import com.activepersistence.service.Relation;
import static java.util.Optional.ofNullable;
import java.util.function.Supplier;

public interface QueryMethods<T> {

    public Relation<T> thiz();

    public Relation<T> spawn();

    public Values getValues();

    public Base<T> getService();

    public default Relation<T> all() {
        if (thiz().getCurrentScope() != null) {
            return new Relation(thiz().getCurrentScope());
        } else {
            return defaultScoped();
        }
    }

    public default Relation<T> select(String... fields) {
        return spawn().select_(fields);
    }

    public default Relation<T> select_(String... fields) {
        getValues().addSelect(fields); return thiz();
    }

    public default Relation<T> joins(String value) {
        return spawn().joins_(value);
    }

    public default Relation<T> joins_(String value) {
        getValues().addJoins(value); return thiz();
    }

    public default Relation<T> where(String conditions) {
        return spawn().where_(conditions);
    }

    public default Relation<T> where_(String conditions) {
        getValues().addWhere(conditions); return thiz();
    }

    public default Relation<T> group(String... fields) {
        return spawn().group_(fields);
    }

    public default Relation<T> group_(String... fields) {
        getValues().addGroup(fields); return thiz();
    }

    public default Relation<T> having(String conditions) {
        return spawn().having_(conditions);
    }

    public default Relation<T> having_(String conditions) {
        getValues().addHaving(conditions); return thiz();
    }

    public default Relation<T> order(String... fields) {
        return spawn().order_(fields);
    }

    public default Relation<T> order_(String... fields) {
        getValues().addOrder(fields); return thiz();
    }

    public default Relation<T> limit(int limit) {
        return spawn().limit_(limit);
    }

    public default Relation<T> limit_(int limit) {
        getValues().setLimitValue(limit); return thiz();
    }

    public default Relation<T> offset(int offset) {
        return spawn().offset_(offset);
    }

    public default Relation<T> offset_(int offset) {
        getValues().setOffsetValue(offset); return thiz();
    }

    public default Relation<T> distinct() {
        return spawn().distinct_(true);
    }

    public default Relation<T> distinct(boolean value) {
        return spawn().distinct_(value);
    }

    public default Relation<T> distinct_(boolean value) {
        getValues().setDistinctValue(value); return thiz();
    }

    public default Relation<T> none() {
        return spawn().none_();
    }

    public default Relation<T> none_() {
        thiz().where_("1=0"); return thiz();
    }

    public default Relation<T> includes(String... includes) {
        return spawn().includes_(includes);
    }

    public default Relation<T> includes_(String... includes) {
        getValues().addIncludes(includes); return thiz();
    }

    public default Relation<T> eagerLoads(String... eagerLoads) {
        return spawn().eagerLoads_(eagerLoads);
    }

    public default Relation<T> eagerLoads_(String... eagerLoads) {
        getValues().addEagerLoads(eagerLoads); return thiz();
    }

    public default Relation<T> lock() {
        return spawn().lock_(true);
    }

    public default Relation<T> lock(boolean value) {
        return spawn().lock_(value);
    }

    public default Relation<T> lock_(boolean value) {
        getValues().setLockValue(value); return thiz();
    }

    public default Relation<T> from(String value) {
        return spawn().from_(value);
    }

    public default Relation<T> from_(String value) {
        getValues().setFromClause(value); return thiz();
    }

    public default Relation<T> unscope(ValidUnscopingValues... values) {
        return spawn().unscope_(values);
    }

    public default Relation<T> unscope_(ValidUnscopingValues... values) {
        for (ValidUnscopingValues value : values) {
            switch (value) {
                case SELECT:
                    getValues().clearSelect();
                case FROM:
                    getValues().setFromClause(null);
                case JOINS:
                    getValues().clearJoins();
                case WHERE:
                    getValues().clearWhere();
                case GROUP:
                    getValues().clearGroup();
                case HAVING:
                    getValues().clearHaving();
                case ORDER:
                    getValues().clearOrder();
                case LIMIT:
                    getValues().setLimitValue(0);
                case OFFSET:
                    getValues().setOffsetValue(0);
                case INCLUDES:
                    getValues().clearIncludes();
                case EAGER_LOADS:
                    getValues().clearEagerLoads();
                case LOCK:
                    getValues().setLockValue(false);
            }
        }
        return thiz();
    }

    public default Relation<T> reselect(String... fields) {
        return spawn().unscope(ValidUnscopingValues.SELECT).select(fields);
    }

    public default Relation<T> rewhere(String conditions) {
        return spawn().unscope(ValidUnscopingValues.WHERE).where(conditions);
    }

    public default Relation<T> reorder(String... fields) {
        return spawn().unscope(ValidUnscopingValues.ORDER).order(fields);
    }

    public default Relation<T> bind(int position, Object value) {
        return spawn().bind_(position, value);
    }

    public default Relation<T> bind_(int position, Object value) {
        getValues().addOrdinalParameter(position, value); return thiz();
    }

    public default Relation<T> bind(String name, Object value) {
        return spawn().bind_(name, value);
    }

    public default Relation<T> bind_(String name, Object value) {
        getValues().addNamedParameter(name, value); return thiz();
    }

    private Relation<T> defaultScoped() {
        return new Relation(ofNullable(buildDefaultScope()).orElse(thiz()));
    }

    private Relation<T> buildDefaultScope() {
        if (getService().useDefaultScope()) {
            return evaluateDefaultScope(() -> getService().defaultScope());
        } else {
            return null;
        }
    }

    // The ignoreDefaultScope flag is used to prevent an infinite recursion
    // situation where a default scope references a scope which has a default
    // scope which references a scope...
    private Relation<T> evaluateDefaultScope(Supplier<Relation> supplier) {
        if (getService().shouldIgnoreDefaultScope()) return null;

        try {
            getService().setIgnoreDefaultScope(true);
            return supplier.get();
        } finally {
            getService().setIgnoreDefaultScope(false);
        }
    }
}
