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

    public default Relation<T> eagerLoads(String... eagerLoads) {
        return spawn().eagerLoads$(eagerLoads);
    }

    public default Relation<T> eagerLoads$(String... eagerLoads) {
        getValues().getEagerLoads().addAll(asList(eagerLoads)); return thiz();
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

    public default Relation<T> unscope(String... values) {
        return spawn().unscope$(values);
    }

    public default Relation<T> unscope$(String... args) {
        getValues().getUnscope().addAll(asList(args)); asList(args).forEach(this::unscoping); return thiz();
    }

    public default Relation<T> reselect(String... fields) {
        return spawn().except("select", "constructor").select(fields);
    }

    public default Relation<T> rewhere(String conditions) {
        return spawn().except("where", "bind").where(conditions);
    }

    public default Relation<T> reorder(String... fields) {
        return spawn().except("order").order(fields);
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

    private void unscoping(String scope) {
        switch (scope) {
            case "from":       getValues().setFrom(null); break;

            case "where":      getValues().getWhere().clear();
                               getValues().getBind().clear();
                               break;

            case "having":     getValues().getHaving().clear();
                               getValues().getBind().clear();
                               break;

            case "limit":      getValues().setLimit(0);    break;
            case "offset":     getValues().setOffset(0);   break;
            case "lock":       getValues().setLock(false); break;

            case "select":     getValues().getSelect().clear();
                               getValues().setConstructor(false);
                               break;

            case "group":      getValues().getGroup().clear(); break;
            case "order":      getValues().getOrder().clear(); break;
            case "joins":      getValues().getJoins().clear(); break;
            case "includes":   getValues().getIncludes().clear();   break;
            case "eagerLoads": getValues().getEagerLoads().clear(); break;

            default: throw new RuntimeException("invalid unscoping value: " + scope);
        }
    }
}
