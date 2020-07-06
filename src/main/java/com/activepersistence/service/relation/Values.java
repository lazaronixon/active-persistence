package com.activepersistence.service.relation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Values {

    private String fromClause     = null;

    private int limitValue        = 0;
    private int offsetValue       = 0;
    private boolean lockValue     = false;
    private boolean distinctValue = false;
    private boolean constructor   = false;

    private List<String> selectValues     = new ArrayList();
    private List<String> whereValues      = new ArrayList();
    private List<String> groupValues      = new ArrayList();
    private List<String> havingValues     = new ArrayList();
    private List<String> orderValues      = new ArrayList();
    private List<String> joinsValues      = new ArrayList();
    private List<String> includesValues   = new ArrayList();
    private List<String> eagerLoadsValues = new ArrayList();

    private HashMap<Integer, Object> ordinalParameters = new HashMap();
    private HashMap<String, Object> namedParameters    = new HashMap();

    public Values() {}

    public Values(Values other) {
        constructor       = other.constructor;
        fromClause        = other.fromClause;
        limitValue        = other.limitValue;
        offsetValue       = other.offsetValue;
        lockValue         = other.lockValue;
        distinctValue     = other.distinctValue;
        selectValues      = new ArrayList(other.selectValues);
        whereValues       = new ArrayList(other.whereValues);
        groupValues       = new ArrayList(other.groupValues);
        havingValues      = new ArrayList(other.havingValues);
        orderValues       = new ArrayList(other.orderValues);
        joinsValues       = new ArrayList(other.joinsValues);
        includesValues    = new ArrayList(other.includesValues);
        eagerLoadsValues  = new ArrayList(other.eagerLoadsValues);
        ordinalParameters = new HashMap(other.ordinalParameters);
        namedParameters   = new HashMap(other.namedParameters);
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

    public boolean isConstructor() {
        return constructor;
    }

    public void setSelectValues(List<String> selectValues) {
        this.selectValues = selectValues;
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

    public void setConstructor(boolean constructor) {
        this.constructor = constructor;
    }

}
