package com.activepersistence.service.relation;

import java.util.ArrayList;
import static java.util.Arrays.asList;
import java.util.HashMap;
import java.util.List;
import java.util.function.Predicate;

public class Values {

    private String from = null;

    private int limit           = 0;
    private int offset          = 0;
    private boolean lock        = false;
    private boolean distinct    = false;
    private boolean constructor = false;

    private List<String> select        = new ArrayList();
    private List<String> where         = new ArrayList();
    private List<String> group         = new ArrayList();
    private List<String> having        = new ArrayList();
    private List<String> order         = new ArrayList();
    private List<String> joins         = new ArrayList();
    private List<String> includes      = new ArrayList();
    private List<String> eagerLoads    = new ArrayList();
    private List<ValueMethods> unscope = new ArrayList();

    private HashMap<Object, Object> bind = new HashMap();

    public Values() {}

    public Values(Values other) {
        constructor  = other.constructor;
        from         = other.from;
        limit        = other.limit;
        offset       = other.offset;
        lock         = other.lock;
        distinct     = other.distinct;
        select       = new ArrayList(other.select);
        where        = new ArrayList(other.where);
        group        = new ArrayList(other.group);
        having       = new ArrayList(other.having);
        order        = new ArrayList(other.order);
        joins        = new ArrayList(other.joins);
        includes     = new ArrayList(other.includes);
        eagerLoads   = new ArrayList(other.eagerLoads);
        unscope      = new ArrayList(other.unscope);
        bind         = new HashMap(other.bind);
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

    public List<String> getEagerLoads() {
        return eagerLoads;
    }

    public List<ValueMethods> getUnscope() {
        return unscope;
    }

    public HashMap<Object, Object> getBind() {
        return bind;
    }

    public int getLimit() {
        return limit;
    }

    public int getOffset() {
        return offset;
    }

    public boolean isLock() {
        return lock;
    }

    public boolean isDistinct() {
        return distinct;
    }

    public boolean isConstructor() {
        return constructor;
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

    public void setLock(boolean lock) {
        this.lock = lock;
    }

    public void setConstructor(boolean constructor) {
        this.constructor = constructor;
    }

    public Values except(List<ValueMethods> skips) {
        return dup().except$(skips);
    }

    public Values except$(List<ValueMethods> skips) {
        skips.forEach(this::reset); return this;
    }

    public Values slice(List<ValueMethods> onlies) {
        return dup().slice$(onlies);
    }

    public Values slice$(List<ValueMethods> onlies) {
        asList(ValueMethods.values()).stream().filter(not(onlies::contains)).forEach(this::reset); return this;
    }

    private Values dup() {
        return new Values(this);
    }

    private <T> Predicate<T> not(Predicate<T> t) {
        return t.negate();
    }

    private void reset(ValueMethods value) {
        switch (value) {
            case FROM:        from = null;         break;
            case WHERE:       where.clear();       break;
            case HAVING:      having.clear();      break;

            case LIMIT:       limit  = 0;          break;
            case OFFSET:      offset = 0;          break;
            case LOCK:        lock = false;        break;
            case DISTINCT:    distinct = false;    break;
            case CONSTRUCTOR: constructor = false; break;

            case SELECT:      select.clear();     break;
            case GROUP:       group.clear();      break;
            case ORDER:       order.clear();      break;
            case JOINS:       joins.clear();      break;
            case INCLUDES:    includes.clear();   break;
            case EAGER_LOAD:  eagerLoads.clear(); break;
            case UNSCOPE:     unscope.clear();    break;
            case BIND:        bind.clear();       break;
        }
    }

}
