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

    private String constructor = null;

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
        this.constructor       = other.constructor;
    }

    public String getFromClause() {
        return fromClause;
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

    public int getLimitValue() {
        return limitValue;
    }

    public int getOffsetValue() {
        return offsetValue;
    }

    public boolean isLockValue() {
        return lockValue;
    }

    public boolean isDistinctValue() {
        return distinctValue;
    }

    public String getConstructor() {
        return constructor;
    }

    public void setFromClause(String fromClause) {
        this.fromClause = fromClause;
    }

    public void setLimitValue(int limitValue) {
        this.limitValue = limitValue;
    }

    public void setOffsetValue(int offsetValue) {
        this.offsetValue = offsetValue;
    }

    public void setDistinctValue(boolean distinctValue) {
        this.distinctValue = distinctValue;
    }

    public void setLockValue(boolean lockValue) {
        this.lockValue = lockValue;
    }

    public void setConstructor(String constructor) {
        this.constructor = constructor;
    }

}
