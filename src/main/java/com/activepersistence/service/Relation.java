package com.activepersistence.service;

import com.activepersistence.service.arel.Entity;
import com.activepersistence.service.arel.SelectManager;
import com.activepersistence.service.relation.Calculation;
import com.activepersistence.service.relation.FinderMethods;
import com.activepersistence.service.relation.QueryMethods;
import com.activepersistence.service.relation.Values;
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

    private final Values values;

    private SelectManager arel;

    private Relation<T> currentScope;

    public Relation(Base service) {
        this.entityManager = service.getEntityManager();
        this.entityClass   = service.getEntityClass();
        this.service       = service;
        this.entity        = new Entity(entityClass, "this");
        this.values        = new Values();
    }

    public Relation(Relation<T> other) {
        this.currentScope        = this;
        this.entityManager       = other.entityManager;
        this.entityClass         = other.entityClass;
        this.service             = other.service;
        this.entity              = other.entity;
        this.values              = new Values(other.values);
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

    @Override
    public Relation<T> getCurrentScope() {
        return currentScope;
    }

    @Override
    public Relation<T> getDefaultScope() {
        return service.defaultScope();
    }

    @Override
    public Values getValues() {
        return values;
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

    public SelectManager getArel() {
        return arel = arel != null ? arel : buildArel();
    }

    private SelectManager buildArel() {
        SelectManager result = new SelectManager(entity);

        values.getJoinsValues().forEach(join    -> result.join(join));
        values.getWhereValues().forEach(where   -> result.where(where));
        values.getHavingValues().forEach(having -> result.having(having));
        values.getGroupValues().forEach(group   -> result.group(group));
        values.getOrderValues().forEach(order   -> result.order(order));

        buildDistinct(result);
        buildSelect(result);
        buildFrom(result);

        return result;
    }

    private void buildDistinct(SelectManager arel) {
        arel.distinct(values.isDistinctValue());
    }

    private void buildSelect(SelectManager arel) {
        if (values.getSelectValues().isEmpty()) {
            arel.project("this");
        } else {
            values.getSelectValues().forEach(select -> arel.constructor(true).project(select));
        }
    }

    private void buildFrom(SelectManager arel) {
        if (values.getFromClause() != null) arel.from(values.getFromClause().getArel().constructor(false));
    }

    private TypedQuery<T> buildQuery() {
        return parametize(service.buildQuery(toJpql())).setLockMode(buildLockMode()).setMaxResults(values.getLimitValue()).setFirstResult(values.getOffsetValue());
    }

    private Query buildQuery_() {
        return parametize(service.buildQuery_(toJpql())).setLockMode(buildLockMode()).setMaxResults(values.getLimitValue()).setFirstResult(values.getOffsetValue());
    }

    private <R> TypedQuery<R> parametize(TypedQuery<R> query) {
        applyParams(query); applyHints(query); return query;
    }

    private Query parametize(Query query) {
        applyParams(query); applyHints(query); return query;
    }

    private void applyParams(Query query) {
        values.getOrdinalParameters().entrySet().forEach(p -> query.setParameter(p.getKey(), p.getValue()));
        values.getNamedParameters().entrySet().forEach(p   -> query.setParameter(p.getKey(), p.getValue()));
    }

    private void applyHints(Query query) {
        values.getIncludesValues().forEach(value   -> query.setHint("eclipselink.batch", value));
        values.getEagerLoadsValues().forEach(value -> query.setHint("eclipselink.left-join-fetch", value));
    }

    private LockModeType buildLockMode() {
        return values.isLockValue() ? PESSIMISTIC_READ : NONE;
    }

}
