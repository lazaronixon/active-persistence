package com.activepersistence.service.relation;

import com.activepersistence.service.Relation;
import com.activepersistence.service.relation.QueryMethods.ValidUnscopingValues;
import static com.activepersistence.service.relation.QueryMethods.ValidUnscopingValues.ORDER;
import static com.activepersistence.service.relation.QueryMethods.ValidUnscopingValues.SELECT;
import static com.activepersistence.service.relation.QueryMethods.ValidUnscopingValues.WHERE;
import static java.util.Arrays.asList;
import java.util.List;
import java.util.stream.Stream;

public interface QueryMethods<T> {

    public enum ValidUnscopingValues {
        WHERE, SELECT, GROUP, ORDER, LOCK, LIMIT, OFFSET, JOINS, INCLUDES, EAGER_LOADS, FROM, HAVING
    }

    public Relation<T> thiz();

    public Relation<T> spawn();

    public Values getValues();

    public default Relation<T> select(String... fields) {
        return spawn().select_(fields);
    }

    public default Relation<T> select_(String... fields) {
        getValues().getSelectValues().addAll(asList(fields));
        getValues().setConstructor(true);
        return thiz();
    }

    public default Relation<T> joins(String value) {
        return spawn().joins_(value);
    }

    public default Relation<T> joins_(String value) {
        getValues().getJoinsValues().add(value); return thiz();
    }

    public default Relation<T> where(String conditions) {
        return spawn().where_(conditions);
    }

    public default Relation<T> where_(String conditions) {
        getValues().getWhereValues().add(conditions); return thiz();
    }

    public default Relation<T> group(String... fields) {
        return spawn().group_(fields);
    }

    public default Relation<T> group_(String... fields) {
        getValues().getGroupValues().addAll(asList(fields)); return thiz();
    }

    public default Relation<T> having(String conditions) {
        return spawn().having_(conditions);
    }

    public default Relation<T> having_(String conditions) {
        getValues().getHavingValues().add(conditions); return thiz();
    }

    public default Relation<T> order(String... fields) {
        return spawn().order_(fields);
    }

    public default Relation<T> order_(String... fields) {
        getValues().getOrderValues().addAll(asList(fields)); return thiz();
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
        where_("1=0"); return thiz();
    }

    public default Relation<T> includes(String... includes) {
        return spawn().includes_(includes);
    }

    public default Relation<T> includes_(String... includes) {
        getValues().getIncludesValues().addAll(asList(includes)); return thiz();
    }

    public default Relation<T> eagerLoads(String... eagerLoads) {
        return spawn().eagerLoads_(eagerLoads);
    }

    public default Relation<T> eagerLoads_(String... eagerLoads) {
        getValues().getEagerLoadsValues().addAll(asList(eagerLoads)); return thiz();
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
        getValues().setFromClause(new FromClause(value)); return thiz();
    }

    public default Relation<T> from(Relation relation) {
        return spawn().from_(relation);
    }

    public default Relation<T> from_(Relation relation) {
        getValues().setFromClause(new FromClause(relation, "subquery")); return thiz();
    }

    public default Relation<T> unscope(ValidUnscopingValues... values) {
        return spawn().unscope_(values);
    }

    public default Relation<T> unscope_(ValidUnscopingValues... values) {
        asList(values).forEach(this::unscope_); return thiz();
    }

    public default Relation<T> except(ValidUnscopingValues... values) {
        return unscope(values);
    }

    public default Relation<T> only(ValidUnscopingValues... values) {
        return unscope(allScopesExcept(values));
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
        getValues().getOrdinalParameters().put(position, value); return thiz();
    }

    public default Relation<T> bind(String name, Object value) {
        return spawn().bind_(name, value);
    }

    public default Relation<T> bind_(String name, Object value) {
        getValues().getNamedParameters().put(name, value); return thiz();
    }

    private ValidUnscopingValues[] allScopesExcept(ValidUnscopingValues[] values) {
        List<ValidUnscopingValues> scopes     = asList(ValidUnscopingValues.values());
        List<ValidUnscopingValues> values_    = asList(values);
        Stream<ValidUnscopingValues> unscopes = scopes.stream().filter((v) -> !values_.contains(v));
        return unscopes.toArray(ValidUnscopingValues[]::new);
    }

    private void unscope_(ValidUnscopingValues value) {
        switch (value) {
            case SELECT:
                getValues().getSelectValues().clear();
                getValues().setConstructor(false);
                break;
            case FROM:
                getValues().setFromClause(new FromClause());
                break;
            case JOINS:
                getValues().getJoinsValues().clear();
                break;
            case WHERE:
                getValues().getWhereValues().clear();
                break;
            case GROUP:
                getValues().getGroupValues().clear();
                break;
            case HAVING:
                getValues().getHavingValues().clear();
                break;
            case ORDER:
                getValues().getOrderValues().clear();
                break;
            case LIMIT:
                getValues().setLimitValue(0);
                break;
            case OFFSET:
                getValues().setOffsetValue(0);
                break;
            case INCLUDES:
                getValues().getIncludesValues().clear();
                break;
            case EAGER_LOADS:
                getValues().getEagerLoadsValues().clear();
                break;
            case LOCK:
                getValues().setLockValue(false);
                break;

        }
    }

}
