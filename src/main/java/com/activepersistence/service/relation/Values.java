package com.activepersistence.service.relation;

import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Set;

public class Values {

    private String fromClause     = null;
    private int limitValue        = 0;
    private int offsetValue       = 0;
    private boolean lockValue     = false;
    private boolean distinctValue = false;
    private boolean constructor   = false;

    private Set<String> selectValues     = new LinkedHashSet();
    private Set<String> whereValues      = new LinkedHashSet();
    private Set<String> groupValues      = new LinkedHashSet();
    private Set<String> havingValues     = new LinkedHashSet();
    private Set<String> orderValues      = new LinkedHashSet();
    private Set<String> joinsValues      = new LinkedHashSet();
    private Set<String> includesValues   = new LinkedHashSet();
    private Set<String> eagerLoadsValues = new LinkedHashSet();

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
        selectValues      = new LinkedHashSet(other.selectValues);
        whereValues       = new LinkedHashSet(other.whereValues);
        groupValues       = new LinkedHashSet(other.groupValues);
        havingValues      = new LinkedHashSet(other.havingValues);
        orderValues       = new LinkedHashSet(other.orderValues);
        joinsValues       = new LinkedHashSet(other.joinsValues);
        includesValues    = new LinkedHashSet(other.includesValues);
        eagerLoadsValues  = new LinkedHashSet(other.eagerLoadsValues);
        ordinalParameters = new HashMap(other.ordinalParameters);
        namedParameters   = new HashMap(other.namedParameters);
    }

    public Values merge(Values other) {
        if (other.getFromClause()  != null) fromClause  = other.getFromClause();
        if (other.getLimitValue()  != 0) limitValue  = other.getLimitValue();
        if (other.getOffsetValue() != 0) offsetValue = other.getOffsetValue();
        if (other.isLockValue()) lockValue = other.isLockValue();
        if (other.isDistinctValue()) distinctValue = other.isDistinctValue();
        if (other.isConstructor()) constructor = other.isConstructor();
        selectValues.addAll(other.getSelectValues());
        whereValues.addAll(other.getWhereValues());
        groupValues.addAll(other.getGroupValues());
        havingValues.addAll(other.getHavingValues());
        orderValues.addAll(other.getOrderValues());
        joinsValues.addAll(other.getJoinsValues());
        includesValues.addAll(other.getIncludesValues());
        eagerLoadsValues.addAll(other.getEagerLoadsValues());
        ordinalParameters.putAll(other.getOrdinalParameters());
        namedParameters.putAll(other.getNamedParameters());
        return this;
    }

    public String getFromClause() {
        return fromClause;
    }

    public Set<String> getSelectValues() {
        return selectValues;
    }

    public Set<String> getWhereValues() {
        return whereValues;
    }

    public Set<String> getGroupValues() {
        return groupValues;
    }

    public Set<String> getHavingValues() {
        return havingValues;
    }

    public Set<String> getOrderValues() {
        return orderValues;
    }

    public Set<String> getJoinsValues() {
        return joinsValues;
    }

    public Set<String> getIncludesValues() {
        return includesValues;
    }

    public Set<String> getEagerLoadsValues() {
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

    public void setSelectValues(Set<String> selectValues) {
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
