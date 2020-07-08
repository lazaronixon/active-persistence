package com.activepersistence.service.relation;

import java.util.ArrayList;
import static java.util.Arrays.asList;
import java.util.HashMap;
import java.util.List;
import java.util.function.Predicate;

public class Values {

    private final static String[] VALUE_METHODS = new String[] {
        "from",
        "limit", "offset", "lock", "distinct", "constructor",
        "select", "where", "group", "having", "order", "joins", "includes", "eagerLoads",
        "ordinalBind", "namedBind"
    };

    private String from = null;

    private int limit           = 0;
    private int offset          = 0;
    private boolean lock        = false;
    private boolean distinct    = false;
    private boolean constructor = false;

    private List<String> select     = new ArrayList();
    private List<String> where      = new ArrayList();
    private List<String> group      = new ArrayList();
    private List<String> having     = new ArrayList();
    private List<String> order      = new ArrayList();
    private List<String> joins      = new ArrayList();
    private List<String> includes   = new ArrayList();
    private List<String> eagerLoads = new ArrayList();
    private List<String> unscope    = new ArrayList();

    private HashMap<Integer, Object> ordinalBind = new HashMap();
    private HashMap<String, Object> namedBind    = new HashMap();

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
        ordinalBind  = new HashMap(other.ordinalBind);
        namedBind    = new HashMap(other.namedBind);
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

    public HashMap<Integer, Object> getOrdinalBind() {
        return ordinalBind;
    }

    public HashMap<String, Object> getNamedBind() {
        return namedBind;
    }

    public int getLimit() {
        return limit;
    }

    public int getOffset() {
        return offset;
    }

    public List<String> getUnscope() {
        return unscope;
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

    public Values except(List<String> skips) {
        return dup().except_(skips);
    }

    public Values except_(List<String> skips) {
        skips.forEach(this::reset); return this;
    }

    public Values slice(List<String> onlies) {
        return dup().slice_(onlies);
    }

    public Values slice_(List<String> onlies) {
        asList(VALUE_METHODS).stream().filter(not(onlies::contains)).forEach(this::reset); return this;
    }

    public Values dup() {
        return new Values(this);
    }

    public void reset(String value) {
        switch (value) {
            case "from": from = null; break;

            case "limit": limit = 0; break;
            case "offset": offset = 0; break;
            case "lock": lock = false; break;
            case "distinct": distinct = false; break;
            case "constructor": constructor = false; break;

            case "select": select.clear(); break;
            case "where": where.clear();  break;
            case "group": group.clear();  break;
            case "having": having.clear(); break;
            case "order": order.clear();  break;
            case "joins": joins.clear();  break;
            case "includes": includes.clear(); break;
            case "eagerLoads": eagerLoads.clear(); break;

            case "ordinalBind": ordinalBind.clear(); break;
            case "namedBind": namedBind.clear(); break;
        }
    }

    public void unscoping(String value) {
        switch (value) {
            case "from": from = null; break;

            case "where":   where.clear(); namedBind.clear(); ordinalBind.clear(); break;
            case "having": having.clear(); namedBind.clear(); ordinalBind.clear(); break;

            case "limit":   limit = 0; break;
            case "offset": offset = 0; break;
            case "lock": lock = false; break;

            case "select": select.clear(); constructor = false; break;
            case "group": group.clear(); break;
            case "order": order.clear(); break;
            case "joins": joins.clear(); break;
            case "includes": includes.clear(); break;
            case "eagerLoads": eagerLoads.clear(); break;
        }
    }

    private static <T> Predicate<T> not(Predicate<T> t) {
        return t.negate();
    }

}
