package com.activepersistence.service;

import com.activepersistence.service.arel.Entity;
import com.activepersistence.service.arel.SelectManager;
import com.activepersistence.service.relation.Calculation;
import com.activepersistence.service.relation.FinderMethods;
import com.activepersistence.service.relation.QueryMethods;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.LockModeType;
import static javax.persistence.LockModeType.NONE;
import static javax.persistence.LockModeType.PESSIMISTIC_READ;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

public class Relation<T> implements FinderMethods<T>, QueryMethods<T>, Calculation<T> {

    private final EntityManager entityManager;

    private final Class<T> entityClass;

    private final Base<T> service;

    private final Entity entity;

    private Class fromClause = null;

    private List<String> selectValues = new ArrayList();

    private List<String> whereValues  = new ArrayList();

    private List<String> groupValues  = new ArrayList();

    private List<String> havingValues = new ArrayList();

    private List<String> orderValues  = new ArrayList();

    private List<String> joinsValues  = new ArrayList();

    private List<String> includesValues = new ArrayList();

    private List<String> eagerLoadsValues = new ArrayList();

    private HashMap<Integer, Object> ordinalParameters = new HashMap();

    private HashMap<String, Object> namedParameters = new HashMap();

    private int limitValue  = 0;

    private int offsetValue = 0;

    private boolean lockValue = false;

    private boolean distinctValue = false;

    private Relation<T> currentScope = null;

    public Relation(Base service) {
        this.entityManager = service.getEntityManager();
        this.entityClass   = service.getEntityClass();
        this.service       = service;
        this.entity        = new Entity(entityClass);
    }

    public Relation(Relation<T> other) {
        this.currentScope        = this;
        this.entityManager       = other.entityManager;
        this.entityClass         = other.entityClass;
        this.service             = other.service;
        this.entity              = other.entity;
        this.selectValues        = new ArrayList(other.selectValues);
        this.whereValues         = new ArrayList(other.whereValues);
        this.groupValues         = new ArrayList(other.groupValues);
        this.havingValues        = new ArrayList(other.havingValues);
        this.orderValues         = new ArrayList(other.orderValues);
        this.joinsValues         = new ArrayList(other.joinsValues);
        this.includesValues      = new ArrayList(other.includesValues);
        this.eagerLoadsValues    = new ArrayList(other.eagerLoadsValues);
        this.ordinalParameters   = new HashMap(other.ordinalParameters);
        this.namedParameters     = new HashMap(other.namedParameters);
        this.fromClause          = other.fromClause;
        this.limitValue          = other.limitValue;
        this.offsetValue         = other.offsetValue;
        this.lockValue           = other.lockValue;
        this.distinctValue       = other.distinctValue;
    }

    public T fetchOne() {
        return buildQuery().getResultStream().findFirst().orElse(null);
    }

    public T fetchOneOrFail() {
        return buildQuery().getSingleResult();
    }

    public List<T> fetch() {
        return buildQuery().getResultList();
    }

    public List fetch_() {
        return buildQuery_().getResultList();
    }

    public boolean fetchExists() {
        return buildQuery().getResultStream().findAny().isPresent();
    }

    public Relation<T> scoping(Relation relation) {
        return new Relation(relation);
    }

    public Relation<T> unscoped() {
        return new Relation(service);
    }

    public String toJpql() {
        return buildArel().toJpql() ;
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

    public void setFromClause(Class fromClause) {
        this.fromClause = fromClause;
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

    public boolean isDistinctValue() {
        return distinctValue;
    }

    public void clearFrom() {
        this.fromClause = null;
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

    public Relation<T> getCurrentScope() {
        return currentScope;
    }

    public Relation<T> getDefaultScope() {
        return service.defaultScope();
    }

    @Override
    public Base<T> getService() {
        return service;
    }

    @Override
    public Relation<T> spawn() {
        return new Relation(this);
    }

    @Override
    public Relation<T> thiz() {
        return this;
    }

    private SelectManager buildArel() {
        SelectManager arel = new SelectManager(entity);

        joinsValues.forEach(join    -> arel.join(join));
        whereValues.forEach(where   -> arel.where(where));
        havingValues.forEach(having -> arel.having(having));
        groupValues.forEach(group   -> arel.group(group));
        orderValues.forEach(order   -> arel.order(order));

        buildDistinct(arel);
        buildSelect(arel);
        buildFrom(arel);

        return arel;
    }

    private void buildDistinct(SelectManager arel) {
        arel.distinct(distinctValue);
    }

    private void buildSelect(SelectManager arel) {
        if (selectValues.isEmpty()) {
            arel.project("this");
        } else {
            arel.constructor(true);
            selectValues.forEach(select -> arel.project(select));
        }
    }

    private void buildFrom(SelectManager arel) {
        if (fromClause != null) arel.from(fromClause);
    }

    private TypedQuery<T> buildQuery() {
        return parametize(service.buildQuery(toJpql())).setLockMode(buildLockMode()).setMaxResults(limitValue).setFirstResult(offsetValue);
    }

    private Query buildQuery_() {
        return parametize(service.buildQuery_(toJpql())).setLockMode(buildLockMode()).setMaxResults(limitValue).setFirstResult(offsetValue);
    }

    private <R> TypedQuery<R> parametize(TypedQuery<R> query) {
        applyParams(query); applyHints(query); return query;
    }

    private Query parametize(Query query) {
        applyParams(query); applyHints(query); return query;
    }

    private void applyParams(Query query) {
        ordinalParameters.entrySet().forEach(p -> query.setParameter(p.getKey(), p.getValue()));
        namedParameters.entrySet().forEach(p   -> query.setParameter(p.getKey(), p.getValue()));
    }

    private void applyHints(Query query) {
        includesValues.forEach(value   -> query.setHint("eclipselink.batch", value));
        eagerLoadsValues.forEach(value -> query.setHint("eclipselink.left-join-fetch", value));
    }

    private LockModeType buildLockMode() {
        return lockValue ? PESSIMISTIC_READ : NONE;
    }

}
