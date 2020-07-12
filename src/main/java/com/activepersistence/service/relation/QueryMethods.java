package com.activepersistence.service.relation;

import com.activepersistence.service.Relation;
import static java.util.Arrays.asList;

public interface QueryMethods<T> {

    public Values getValues();

    public Relation<T> spawn();

    public Relation<T> thiz();

    public default Relation<T> select(String... fields) {
        return spawn().select$(fields);
    }

    public default Relation<T> select$(String... fields) {
        getValues().setConstructor(true); getValues().getSelect().addAll(asList(fields)); return thiz();
    }

    public default Relation<T> joins(String value) {
        return spawn().joins$(value);
    }

    public default Relation<T> joins$(String value) {
        getValues().getJoins().add(value); return thiz();
    }

    public default Relation<T> where(String conditions) {
        return spawn().where$(conditions);
    }

    public default Relation<T> where$(String conditions) {
        getValues().getWhere().add(conditions); return thiz();
    }

    public default Relation<T> group(String... fields) {
        return spawn().group$(fields);
    }

    public default Relation<T> group$(String... fields) {
        getValues().getGroup().addAll(asList(fields)); return thiz();
    }

    public default Relation<T> having(String conditions) {
        return spawn().having$(conditions);
    }

    public default Relation<T> having$(String conditions) {
        getValues().getHaving().add(conditions); return thiz();
    }

    public default Relation<T> order(String... fields) {
        return spawn().order$(fields);
    }

    public default Relation<T> order$(String... fields) {
        getValues().getOrder().addAll(asList(fields)); return thiz();
    }

    public default Relation<T> limit(int limit) {
        return spawn().limit$(limit);
    }

    public default Relation<T> limit$(int limit) {
        getValues().setLimit(limit); return thiz();
    }

    public default Relation<T> offset(int offset) {
        return spawn().offset$(offset);
    }

    public default Relation<T> offset$(int offset) {
        getValues().setOffset(offset); return thiz();
    }

    public default Relation<T> distinct() {
        return spawn().distinct$(true);
    }

    public default Relation<T> distinct(boolean value) {
        return spawn().distinct$(value);
    }

    public default Relation<T> distinct$(boolean value) {
        getValues().setDistinct(value); return thiz();
    }

    public default Relation<T> none() {
        return spawn().none$();
    }

    public default Relation<T> none$() {
        where$("1=0"); return thiz();
    }

    public default Relation<T> includes(String... includes) {
        return spawn().includes$(includes);
    }

    public default Relation<T> includes$(String... includes) {
        getValues().getIncludes().addAll(asList(includes)); return thiz();
    }

    public default Relation<T> eagerLoad(String... eagerLoads) {
        return spawn().eagerLoad$(eagerLoads);
    }

    public default Relation<T> eagerLoad$(String... eagerLoads) {
        getValues().getEagerLoad().addAll(asList(eagerLoads)); return thiz();
    }

    public default Relation<T> lock() {
        return spawn().lock$(true);
    }

    public default Relation<T> lock(boolean value) {
        return spawn().lock$(value);
    }

    public default Relation<T> lock$(boolean value) {
        getValues().setLock(value); return thiz();
    }

    public default Relation<T> from(String value) {
        return spawn().from$(value);
    }

    public default Relation<T> from$(String value) {
        getValues().setFrom(value); return thiz();
    }

    public default Relation<T> unscope(ValueMethods... values) {
        return spawn().unscope$(values);
    }

    public default Relation<T> unscope$(ValueMethods... args) {
        getValues().getUnscope().addAll(asList(args)); asList(args).forEach(this::unscoping); return thiz();
    }

    public default Relation<T> reselect(String... fields) {
        return spawn().except(ValueMethods.SELECT, ValueMethods.CONSTRUCTOR).select(fields);
    }

    public default Relation<T> rewhere(String conditions) {
        return spawn().except(ValueMethods.WHERE, ValueMethods.BIND).where(conditions);
    }

    public default Relation<T> reorder(String... fields) {
        return spawn().except(ValueMethods.ORDER).order(fields);
    }

    public default Relation<T> bind(int position, Object value) {
        return spawn().bind$(position, value);
    }

    public default Relation<T> bind(String name, Object value) {
        return spawn().bind$(name, value);
    }

    public default Relation<T> bind$(Object key, Object value) {
        getValues().getBind().put(key, value); return thiz();
    }

    private void unscoping(ValueMethods scope) {
        switch (scope) {
            case WHERE:      getValues().getWhere().clear();
                             getValues().getBind().clear();
                             break;

            case SELECT:     getValues().getSelect().clear();
                             getValues().setConstructor(false);
                             break;

            case GROUP:      getValues().getGroup().clear();    break;
            case ORDER:      getValues().getOrder().clear();    break;
            case LOCK:       getValues().setLock(false);        break;
            case LIMIT:      getValues().setLimit(0);           break;
            case OFFSET:     getValues().setOffset(0);          break;
            case JOINS:      getValues().getJoins().clear();    break;
            case INCLUDES:   getValues().getIncludes().clear(); break;

            case FROM:       getValues().setFrom(null);         break;

            case HAVING:     getValues().getHaving().clear();
                             getValues().getBind().clear();
                             break;

            default: throw new RuntimeException("invalid unscoping value: " + scope);
        }
    }
}
