package com.activepersistence.repository.relation;

import static com.activepersistence.repository.relation.ValueMethods.values;
import java.util.ArrayList;
import static java.util.Arrays.asList;
import static java.util.Arrays.stream;
import java.util.List;
import java.util.function.Predicate;
import javax.persistence.LockModeType;

public class Values {

    private String from = null;

    private int limit           = 0;
    private int offset          = 0;
    private boolean distinct    = false;
    private boolean reordering  = false;
    private boolean constructor = false;
    private boolean readonly    = false;

    private List<String> select          = new ArrayList();
    private List<String> where           = new ArrayList();
    private List<String> group           = new ArrayList();
    private List<String> having          = new ArrayList();
    private List<String> order           = new ArrayList();
    private List<String> joins           = new ArrayList();
    private List<String> includes        = new ArrayList();
    private List<String> eagerLoad       = new ArrayList();
    private List<ValueMethods> unscope   = new ArrayList();

    private LockModeType lock = LockModeType.NONE;

    public Values() {}

    public Values(Values other) {
        constructor    = other.constructor;
        from           = other.from;
        limit          = other.limit;
        offset         = other.offset;
        lock           = other.lock;
        distinct       = other.distinct;
        reordering     = other.reordering;
        readonly       = other.readonly;
        select         = new ArrayList(other.select);
        where          = new ArrayList(other.where);
        group          = new ArrayList(other.group);
        having         = new ArrayList(other.having);
        order          = new ArrayList(other.order);
        joins          = new ArrayList(other.joins);
        includes       = new ArrayList(other.includes);
        eagerLoad      = new ArrayList(other.eagerLoad);
        unscope        = new ArrayList(other.unscope);
    }

    public String getFrom() {
        return from;
    }

    public List<String> getSelect() {
        return select;
    }

    public List<String> getWhere() {
        return where;
    }

    public List<String> getGroup() {
        return group;
    }

    public List<String> getHaving() {
        return having;
    }

    public List<String> getOrder() {
        return order;
    }

    public List<String> getJoins() {
        return joins;
    }

    public List<String> getIncludes() {
        return includes;
    }

    public List<String> getEagerLoad() {
        return eagerLoad;
    }

    public List<ValueMethods> getUnscope() {
        return unscope;
    }

    public int getLimit() {
        return limit;
    }

    public int getOffset() {
        return offset;
    }

    public LockModeType getLock() {
        return lock;
    }

    public boolean isDistinct() {
        return distinct;
    }

    public boolean isConstructor() {
        return constructor;
    }

    public boolean isReordering() {
        return reordering;
    }

    public boolean isReadonly() {
        return readonly;
    }

    public void setSelect(List<String> select) {
        this.select = select;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public void setDistinct(boolean distinct) {
        this.distinct = distinct;
    }

    public void setLock(LockModeType lock) {
        this.lock = lock;
    }

    public void setConstructor(boolean constructor) {
        this.constructor = constructor;
    }

    public void setOrder(List<String> order) {
        this.order = order;
    }

    public void setReordering(boolean reordering) {
        this.reordering = reordering;
    }

    public void setReadonly(boolean readonly) {
        this.readonly = readonly;
    }

    public void setGroup(List<String> group) {
        this.group = group;
    }

    public Values except(ValueMethods... skips) {
        return dup().except$(skips);
    }

    public Values except$(ValueMethods... skips) {
        stream(skips).forEach(this::reset); return this;
    }

    public Values slice(ValueMethods... onlies) {
        return dup().slice$(onlies);
    }

    public Values slice$(ValueMethods... onlies) {
        stream(values()).filter(notContains(onlies)).forEach(this::reset); return this;
    }

    private Predicate<ValueMethods> notContains(ValueMethods[] onlies) {
        return not(asList(onlies)::contains);
    }

    private <T> Predicate<T> not(Predicate<T> t) {
        return t.negate();
    }

    private Values dup() {
        return new Values(this);
    }

    private void reset(ValueMethods value) {
        switch (value) {
            case FROM: from = null; break;

            case LOCK: lock = LockModeType.NONE; break;

            case LIMIT:       limit       = 0;     break;
            case OFFSET:      offset      = 0;     break;
            case DISTINCT:    distinct    = false; break;
            case REORDERING:  reordering  = false; break;
            case CONSTRUCTOR: constructor = false; break;
            case READONLY:   readonly    = false; break;

            case SELECT:     select.clear();    break;
            case WHERE:      where.clear();     break;
            case GROUP:      group.clear();     break;
            case HAVING:     having.clear();    break;
            case ORDER:      order.clear();     break;
            case JOINS:      joins.clear();     break;
            case INCLUDES:   includes.clear();  break;
            case EAGER_LOAD: eagerLoad.clear(); break;
            case UNSCOPE:    unscope.clear();   break;
        }
    }

}
