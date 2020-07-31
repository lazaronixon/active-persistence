package com.activepersistence.service.relation;

import com.activepersistence.service.NullRelation;
import com.activepersistence.service.Relation;
import static com.activepersistence.service.relation.ValueMethods.*;
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
        getValues().getJoins().add(new JoinClause(value)); return thiz();
    }

    public default Relation<T> joins(String path, String alias) {
        return spawn().joins$(path, alias);
    }

    public default Relation<T> joins$(String path, String alias) {
        getValues().getJoins().add(new JoinClause(path, alias)); return thiz();
    }

    public default Relation<T> leftOuterJoins(String path, String alias) {
        return spawn().leftOuterJoins$(path, alias);
    }

    public default Relation<T> leftOuterJoins$(String path, String alias) {
        getValues().getLeftOuterJoins().add(new JoinClause(path, alias)); return thiz();
    }

    public default Relation<T> where(String conditions, Object... params) {
        return spawn().where$(conditions, params);
    }

    public default Relation<T> where$(String conditions, Object... params) {
        getValues().getWhere().add(new WhereClauseFactory(conditions, params).build()); return thiz();
    }

    public default Relation<T> group(String... fields) {
        return spawn().group$(fields);
    }

    public default Relation<T> group$(String... fields) {
        getValues().getGroup().addAll(asList(fields)); return thiz();
    }

    public default Relation<T> having(String conditions, Object... params) {
        return spawn().having$(conditions, params);
    }

    public default Relation<T> having$(String conditions, Object... params) {
        getValues().getHaving().add(new WhereClauseFactory(conditions, params).build()); return thiz();
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

    public default NullRelation<T> none() {
        return new NullRelation(where$("1=0"));
    }

    public default Relation<T> readonly() {
        return spawn().readonly$(true);
    }

    public default Relation<T> readonly(boolean value) {
        return spawn().readonly$(value);
    }

    public default Relation<T> readonly$(boolean value) {
        getValues().setReadonly(value); return thiz();
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
        getValues().setFrom(new FromClause(value)); return thiz();
    }

    public default Relation<T> from(Relation relation) {
        return spawn().from$(relation);
    }

    public default Relation<T> from$(Relation relation) {
        getValues().setFrom(new FromClause(relation, "subquery")); return thiz();
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

    public default Relation<T> rewhere(String conditions, Object... params) {
        return spawn().except(ValueMethods.WHERE).where(conditions, params);
    }

    public default Relation<T> reorder(String... fields) {
        return spawn().reorder$(fields);
    }

    public default Relation<T> reorder$(String... fields) {
        getValues().setReordering(true); getValues().setOrder(asList(fields)); return thiz();
    }

    private void unscoping(ValueMethods scope) {
        switch (scope) {
            case FROM:   getValues().except$(FROM);   break;
            case WHERE:  getValues().except$(WHERE);  break;
            case HAVING: getValues().except$(HAVING); break;

            case SELECT: getValues().except$(SELECT, CONSTRUCTOR); break;

            case GROUP:            getValues().except$(GROUP);            break;
            case ORDER:            getValues().except$(ORDER);            break;
            case LOCK:             getValues().except$(LOCK);             break;
            case LIMIT:            getValues().except$(LIMIT);            break;
            case OFFSET:           getValues().except$(OFFSET);           break;
            case JOINS:            getValues().except$(JOINS);            break;
            case LEFT_OUTER_JOINS: getValues().except$(LEFT_OUTER_JOINS); break;
            case INCLUDES:         getValues().except$(INCLUDES);         break;

            default: throw new RuntimeException("invalid unscoping value: " + scope);
        }
    }
}
