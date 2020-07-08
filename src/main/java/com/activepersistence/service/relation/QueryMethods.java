package com.activepersistence.service.relation;

import com.activepersistence.service.Relation;
import static com.activepersistence.service.relation.QueryMethods.UnscopingValues.ORDER;
import static com.activepersistence.service.relation.QueryMethods.UnscopingValues.SELECT;
import static com.activepersistence.service.relation.QueryMethods.UnscopingValues.WHERE;
import static java.util.Arrays.asList;

public interface QueryMethods<T> {

    public enum UnscopingValues {
        WHERE, SELECT, GROUP, ORDER, LOCK, LIMIT, OFFSET, JOINS, INCLUDES, EAGER_LOADS, FROM, HAVING
    }

    public Relation<T> thiz();

    public Relation<T> spawn();

    public Values getValues();

    public default Relation<T> select(String... fields) {
        return spawn().select_(fields);
    }

    public default Relation<T> select_(String... fields) {
        getValues().getSelect().addAll(asList(fields));
        getValues().setConstructor(true);
        return thiz();
    }

    public default Relation<T> joins(String value) {
        return spawn().joins_(value);
    }

    public default Relation<T> joins_(String value) {
        getValues().getJoins().add(value); return thiz();
    }

    public default Relation<T> where(String conditions) {
        return spawn().where_(conditions);
    }

    public default Relation<T> where_(String conditions) {
        getValues().getWhere().add(conditions); return thiz();
    }

    public default Relation<T> group(String... fields) {
        return spawn().group_(fields);
    }

    public default Relation<T> group_(String... fields) {
        getValues().getGroup().addAll(asList(fields)); return thiz();
    }

    public default Relation<T> having(String conditions) {
        return spawn().having_(conditions);
    }

    public default Relation<T> having_(String conditions) {
        getValues().getHaving().add(conditions); return thiz();
    }

    public default Relation<T> order(String... fields) {
        return spawn().order_(fields);
    }

    public default Relation<T> order_(String... fields) {
        getValues().getOrder().addAll(asList(fields)); return thiz();
    }

    public default Relation<T> limit(int limit) {
        return spawn().limit_(limit);
    }

    public default Relation<T> limit_(int limit) {
        getValues().setLimit(limit); return thiz();
    }

    public default Relation<T> offset(int offset) {
        return spawn().offset_(offset);
    }

    public default Relation<T> offset_(int offset) {
        getValues().setOffset(offset); return thiz();
    }

    public default Relation<T> distinct() {
        return spawn().distinct_(true);
    }

    public default Relation<T> distinct(boolean value) {
        return spawn().distinct_(value);
    }

    public default Relation<T> distinct_(boolean value) {
        getValues().setDistinct(value); return thiz();
    }

    public default Relation<T> none() {
        return spawn().none_();
    }

    public default Relation<T> none_() {
        where_("1=0"); return thiz();
    }

    public default Relation<T> includes(String... includes) {
        return spawn().includes_(includes);
    }

    public default Relation<T> includes_(String... includes) {
        getValues().getIncludes().addAll(asList(includes)); return thiz();
    }

    public default Relation<T> eagerLoads(String... eagerLoads) {
        return spawn().eagerLoads_(eagerLoads);
    }

    public default Relation<T> eagerLoads_(String... eagerLoads) {
        getValues().getEagerLoads().addAll(asList(eagerLoads)); return thiz();
    }

    public default Relation<T> lock() {
        return spawn().lock_(true);
    }

    public default Relation<T> lock(boolean value) {
        return spawn().lock_(value);
    }

    public default Relation<T> lock_(boolean value) {
        getValues().setLock(value); return thiz();
    }

    public default Relation<T> from(String value) {
        return spawn().from_(value);
    }

    public default Relation<T> from_(String value) {
        getValues().setFrom(value); return thiz();
    }

    public default Relation<T> unscope(UnscopingValues... values) {
        return spawn().unscope_(values);
    }

    public default Relation<T> unscope_(UnscopingValues... skips) {
        getValues().except_(asList(skips)); return thiz();
    }

    public default Relation<T> reselect(String... fields) {
        return unscope(SELECT).select(fields);
    }

    public default Relation<T> rewhere(String conditions) {
        return unscope(WHERE).where(conditions);
    }

    public default Relation<T> reorder(String... fields) {
        return unscope(ORDER).order(fields);
    }

    public default Relation<T> bind(int position, Object value) {
        return spawn().bind_(position, value);
    }

    public default Relation<T> bind_(int position, Object value) {
        getValues().getOrdinalBind().put(position, value); return thiz();
    }

    public default Relation<T> bind(String name, Object value) {
        return spawn().bind_(name, value);
    }

    public default Relation<T> bind_(String name, Object value) {
        getValues().getNamedBind().put(name, value); return thiz();
    }

}
