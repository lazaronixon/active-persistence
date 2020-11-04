package com.activepersistence.service.relation;

import com.activepersistence.service.NullRelation;
import com.activepersistence.service.Relation;
import static com.activepersistence.service.Sanitization.sanitizeJpql;
import static com.activepersistence.service.relation.ValueMethods.*;
import static java.util.Arrays.asList;
import java.util.Map;
import javax.persistence.LockModeType;

public interface QueryMethods<T, ID> {

    public Values getValues();

    public Relation<T, ID> spawn();

    public PredicateBuilder getPredicateBuilder();

    public default Relation<T, ID> select(String... fields) {
        return spawn().select$(fields);
    }

    public default Relation<T, ID> select$(String... fields) {
        getValues().setConstructor(true); getValues().getSelect().addAll(asList(fields)); return thiz();
    }

    public default Relation<T, ID> joins(String value) {
        return spawn().joins$(value);
    }

    public default Relation<T, ID> joins$(String value) {
        getValues().getJoins().add(value); return thiz();
    }

    public default Relation<T, ID> where(String conditions, Object... params) {
        return spawn().where$(conditions, params);
    }

    public default Relation<T, ID> where$(String conditions, Object... params) {
        getValues().getWhere().addAll(buildWhereClause(conditions, params)); return thiz();
    }

    public default Relation<T, ID> group(String... fields) {
        return spawn().group$(fields);
    }

    public default Relation<T, ID> group$(String... fields) {
        getValues().getGroup().addAll(asList(fields)); return thiz();
    }

    public default Relation<T, ID> having(String conditions, Object... params) {
        return spawn().having$(conditions, params);
    }

    public default Relation<T, ID> having$(String conditions, Object... params) {
        getValues().getHaving().addAll(buildWhereClause(conditions, params)); return thiz();
    }

    public default Relation<T, ID> order(String... fields) {
        return spawn().order$(fields);
    }

    public default Relation<T, ID> order$(String... fields) {
        getValues().getOrder().addAll(asList(fields)); return thiz();
    }

    public default Relation<T, ID> limit(int limit) {
        return spawn().limit$(limit);
    }

    public default Relation<T, ID> limit$(int limit) {
        getValues().setLimit(limit); return thiz();
    }

    public default Relation<T, ID> offset(int offset) {
        return spawn().offset$(offset);
    }

    public default Relation<T, ID> offset$(int offset) {
        getValues().setOffset(offset); return thiz();
    }

    public default Relation<T, ID> distinct() {
        return spawn().distinct$(true);
    }

    public default Relation<T, ID> distinct(boolean value) {
        return spawn().distinct$(value);
    }

    public default Relation<T, ID> distinct$(boolean value) {
        getValues().setDistinct(value); return thiz();
    }

    public default NullRelation<T, ID> none() {
        return new NullRelation(where$("1=0"));
    }

    public default Relation<T, ID> includes(String... includes) {
        return spawn().includes$(includes);
    }

    public default Relation<T, ID> includes$(String... includes) {
        getValues().getIncludes().addAll(asList(includes)); return thiz();
    }

    public default Relation<T, ID> eagerLoad(String... eagerLoads) {
        return spawn().eagerLoad$(eagerLoads);
    }

    public default Relation<T, ID> eagerLoad$(String... eagerLoads) {
        getValues().getEagerLoad().addAll(asList(eagerLoads)); return thiz();
    }

    public default Relation<T, ID> lock() {
        return spawn().lock$(LockModeType.NONE);
    }

    public default Relation<T, ID> lock(boolean value) {
        return spawn().lock$(value ? LockModeType.PESSIMISTIC_WRITE : LockModeType.NONE);
    }

    public default Relation<T, ID> lock(LockModeType value) {
        return spawn().lock$(value);
    }

    public default Relation<T, ID> lock$(LockModeType value) {
        getValues().setLock(value); return thiz();
    }

    public default Relation<T, ID> from(String value) {
        return spawn().from$(value);
    }

    public default Relation<T, ID> from$(String value) {
        getValues().setFrom(value); return thiz();
    }

    public default Relation<T, ID> unscope(ValueMethods... values) {
        return spawn().unscope$(values);
    }

    public default Relation<T, ID> unscope$(ValueMethods... args) {
        getValues().getUnscope().addAll(asList(args)); asList(args).forEach(this::unscoping); return thiz();
    }

    public default Relation<T, ID> reselect(String... fields) {
        return spawn().except(SELECT, CONSTRUCTOR).select(fields);
    }

    public default Relation<T, ID> rewhere(String conditions, Object... params) {
        return spawn().except(WHERE).where(conditions, params);
    }

    public default Relation<T, ID> reorder(String... fields) {
        return spawn().reorder$(fields);
    }

    public default Relation<T, ID> reorder$(String... fields) {
        getValues().setReordering(true); getValues().setOrder(asList(fields)); return thiz();
    }

    private void unscoping(ValueMethods scope) {
        switch (scope) {
            case FROM:     getValues().except$(FROM);     break;
            case WHERE:    getValues().except$(WHERE);    break;
            case HAVING:   getValues().except$(HAVING);   break;
            case SELECT:   getValues().except$(SELECT, CONSTRUCTOR); break;
            case GROUP:    getValues().except$(GROUP);    break;
            case ORDER:    getValues().except$(ORDER);    break;
            case LOCK:     getValues().except$(LOCK);     break;
            case LIMIT:    getValues().except$(LIMIT);    break;
            case OFFSET:   getValues().except$(OFFSET);   break;
            case JOINS:    getValues().except$(JOINS);    break;
            case INCLUDES: getValues().except$(INCLUDES); break;
            default: throw new RuntimeException("invalid unscoping value: " + scope);
        }
    }

    private WhereClause buildWhereClause(Map conditions) {
        return new WhereClause(asList(getPredicateBuilder().buildFromHash(conditions)));
    }

    private WhereClause buildWhereClause(String conditions, Object[] params) {
        return new WhereClause(asList(sanitizeJpql(conditions, params)));
    }

    private Relation<T, ID> thiz() {
        return (Relation<T, ID>) this;
    }
}
