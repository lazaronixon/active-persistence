package com.activepersistence.service.relation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Values {

   private String fromClause = null;

    private List<String> selectValues = new ArrayList();

    private List<String> whereValues = new ArrayList();

    private List<String> groupValues = new ArrayList();

    private List<String> havingValues = new ArrayList();

    private List<String> orderValues = new ArrayList();

    private List<String> joinsValues = new ArrayList();

    private List<String> includesValues = new ArrayList();

    private List<String> eagerLoadsValues = new ArrayList();

    private HashMap<Integer, Object> ordinalParameters = new HashMap();

    private HashMap<String, Object> namedParameters = new HashMap();

    private int limitValue = 0;

    private int offsetValue = 0;

    private boolean lockValue = false;

    private boolean distinctValue = false;

    public Values() {
    }

    public Values(Values other) {
        this.fromClause        = other.fromClause;
        this.selectValues      = new ArrayList(other.selectValues);
        this.whereValues       = new ArrayList(other.whereValues);
        this.groupValues       = new ArrayList(other.groupValues);
        this.havingValues      = new ArrayList(other.havingValues);
        this.orderValues       = new ArrayList(other.orderValues);
        this.joinsValues       = new ArrayList(other.joinsValues);
        this.includesValues    = new ArrayList(other.includesValues);
        this.eagerLoadsValues  = new ArrayList(other.eagerLoadsValues);
        this.ordinalParameters = new HashMap(other.ordinalParameters);
        this.namedParameters   = new HashMap(other.namedParameters);
        this.limitValue        = other.limitValue;
        this.offsetValue       = other.offsetValue;
        this.lockValue         = other.lockValue;
        this.distinctValue     = other.distinctValue;
    }

    public void setSelect(String select) {
        this.selectValues = List.of(select);
    }

    public void addSelect(String[] select) {
        this.selectValues.addAll(List.of(select));
    }

    public void addJoins(String[] joins) {
        this.joinsValues.addAll(List.of(joins));
    }

    public void addWhere(String where) {
        this.whereValues.add(where);
    }

    public void addHaving(String having) {
        this.havingValues.add(having);
    }

    public void addGroup(String[] group) {
        this.groupValues.addAll(List.of(group));
    }

    public void addOrder(String[] order) {
        this.orderValues.addAll(List.of(order));
    }

    public void addIncludes(String[] includes) {
        includesValues.addAll(List.of(includes));
    }

    public void addEagerLoads(String[] eagerLoads) {
        eagerLoadsValues.addAll(List.of(eagerLoads));
    }

    public void addOrdinalParameter(int position, Object value) {
        ordinalParameters.put(position, value);
    }

    public void addNamedParameter(String name, Object value) {
        namedParameters.put(name, value);
    }

    public void setOffsetValue(int offset) {
        this.offsetValue = offset;
    }

    public void setLimitValue(int limit) {
        this.limitValue = limit;
    }

    public void setDistinctValue(boolean distinct) {
        this.distinctValue = distinct;
    }

    public void setFromClause(String fromClause) {
        this.fromClause = fromClause;
    }

    public String getFromClause() {
        return fromClause;
    }

    public int getLimitValue() {
        return limitValue;
    }

    public boolean isDistinctValue() {
        return distinctValue;
    }

    public void clearSelect() {
        this.selectValues.clear();
    }

    public void clearJoins() {
        this.joinsValues.clear();
    }

    public void clearGroup() {
        this.groupValues.clear();
    }

    public void clearIncludes() {
        this.includesValues.clear();
    }

    public void clearEagerLoads() {
        this.eagerLoadsValues.clear();
    }

    public void clearWhere() {
        this.whereValues.clear();
    }

    public void clearHaving() {
        this.havingValues.clear();
    }

    public void clearOrder() {
        this.orderValues.clear();
    }

    public void setLockValue(boolean lock) {
        this.lockValue = lock;
    }

    public List<String> getSelectValues() {
        return selectValues;
    }

    public List<String> getWhereValues() {
        return whereValues;
    }

    public List<String> getGroupValues() {
        return groupValues;
    }

    public List<String> getHavingValues() {
        return havingValues;
    }

    public List<String> getOrderValues() {
        return orderValues;
    }

    public List<String> getJoinsValues() {
        return joinsValues;
    }

    public List<String> getIncludesValues() {
        return includesValues;
    }

    public List<String> getEagerLoadsValues() {
        return eagerLoadsValues;
    }

    public HashMap<Integer, Object> getOrdinalParameters() {
        return ordinalParameters;
    }

    public HashMap<String, Object> getNamedParameters() {
        return namedParameters;
    }

    public int getOffsetValue() {
        return offsetValue;
    }

    public boolean isLockValue() {
        return lockValue;
    }

}
