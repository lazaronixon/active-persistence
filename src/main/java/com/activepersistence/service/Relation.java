package com.activepersistence.service;

import com.activepersistence.ActivePersistenceError;
import com.activepersistence.service.arel.DeleteManager;
import com.activepersistence.service.arel.Entity;
import com.activepersistence.service.arel.SelectManager;
import com.activepersistence.service.arel.UpdateManager;
import com.activepersistence.service.relation.Calculation;
import com.activepersistence.service.relation.FinderMethods;
import com.activepersistence.service.relation.QueryMethods;
import com.activepersistence.service.relation.Scoping;
import com.activepersistence.service.relation.SpawnMethods;
import com.activepersistence.service.relation.Values;
import java.util.List;
import static java.util.Optional.ofNullable;
import java.util.function.Supplier;
import static java.util.stream.Collectors.toList;
import javax.persistence.EntityManager;
import javax.persistence.LockModeType;
import static javax.persistence.LockModeType.NONE;
import static javax.persistence.LockModeType.PESSIMISTIC_READ;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

public class Relation<T> implements FinderMethods<T>, QueryMethods<T>, Calculation<T>, SpawnMethods<T>, Scoping<T> {

    private final EntityManager entityManager;

    private final Class<T> entityClass;

    private final Base<T> service;

    private final Entity entity;

    private final Values values;

    private SelectManager arel;

    public Relation(Base service) {
        this.entityManager = service.getEntityManager();
        this.entityClass   = service.getEntityClass();
        this.service       = service;
        this.entity        = new Entity(entityClass, "this");
        this.values        = new Values();
    }

    public Relation(Relation<T> other) {
        this.entityManager       = other.entityManager;
        this.entityClass         = other.entityClass;
        this.service             = other.service;
        this.entity              = other.entity;
        this.values              = new Values(other.values);
    }

    public T fetchOne() {
        return buildQuery(toJpql()).getResultStream().findFirst().orElse(null);
    }

    public T fetchOneOrFail() {
        return buildQuery(toJpql()).getSingleResult();
    }

    public List<T> fetch() {
        return buildQuery(toJpql()).getResultList();
    }

    public List fetch_() {
        return buildQuery_(toJpql()).getResultList();
    }

    public boolean fetchExists() {
        return buildQuery(toJpql()).getResultStream().findAny().isPresent();
    }

    public Relation<T> scoping(Relation<T> scope) {
        return scoping(() -> scope);
    }

    public Relation<T> scoping(Supplier<Relation> yield) {
        Relation<T> previous = Scoping.getCurrentScope();
        try {
            Scoping.setCurrentScope(thiz());
            return yield.get();
        } finally {
            Scoping.setCurrentScope(previous);
        }
    }

    public T findOrCreateBy(String conditions, T resource) {
        return ofNullable(findBy(conditions)).orElseGet(() -> service.create(resource));
    }

    public T findOrInitializeBy(String conditions, T resource) {
        return ofNullable(findBy(conditions)).orElseGet(() -> resource);
    }

    public List<T> destroyAll() {
        return fetch().stream().map((r) -> { service.destroy(r); return r; }).collect(toList());
    }

    public List<T> destroyBy(String conditions) {
        return where(conditions).destroyAll();
    }

    public int deleteAll() {
        if (isValidRelationForUpdate()) {
            DeleteManager stmt = new DeleteManager();
            stmt.from(entity);
            stmt.setWheres(getArel().getConstraints());
            stmt.setOrders(getArel().getOrders());
            return executeUpdate(stmt.toJpql());
        } else {
            throw new ActivePersistenceError("delete_all doesn't support this relation");
        }
    }

    public int updateAll(String updates) {
        if (isValidRelationForUpdate()) {
            UpdateManager stmt = new UpdateManager();
            stmt.entity(entity);
            stmt.set(updates);
            stmt.setWheres(getArel().getConstraints());
            stmt.setOrders(getArel().getOrders());
            return executeUpdate(stmt.toJpql());
        } else {
            throw new ActivePersistenceError("update_all doesn't support this relation");
        }
    }

    public int deleteBy(String conditions) {
        return where(conditions).deleteAll();
    }

    public String toJpql() {
        return getArel().toJpql();
    }

    @Override
    public Values getValues() {
        return values;
    }

    @Override
    public String getAlias() {
        return entity.getAlias();
    }

    @Override
    public String getPrimaryKey() {
        return getAlias() + ".id";
    }

    @Override
    public Base<T> getService() {
        return service;
    }

    @Override
    public Relation<T> thiz() {
        return this;
    }

    @Override
    public Relation<T> spawn() {
        return SpawnMethods.super.spawn();
    }

    private SelectManager getArel() {
        return arel = ofNullable(arel).orElseGet(this::buildArel);
    }

    private SelectManager buildArel() {
        SelectManager result = new SelectManager(entity);

        values.getJoinsValues().forEach(join    -> result.join(join));
        values.getWhereValues().forEach(where   -> result.where(where));
        values.getHavingValues().forEach(having -> result.having(having));
        values.getGroupValues().forEach(group   -> result.group(group));
        values.getOrderValues().forEach(order   -> result.order(order));

        buildConstructor(result);
        buildDistinct(result);
        buildSelect(result);
        buildFrom(result);

        return result;
    }

    private TypedQuery<T> buildQuery(String query) {
        return parametize(createTypedQuery(query))
                .setLockMode(buildLockMode())
                .setMaxResults(values.getLimitValue())
                .setFirstResult(values.getOffsetValue())
                .setHint("eclipselink.batch.type", "IN");
    }

    private Query buildQuery_(String query) {
        return parametize(createQuery(query))
                .setLockMode(buildLockMode())
                .setMaxResults(values.getLimitValue())
                .setFirstResult(values.getOffsetValue())
                .setHint("eclipselink.batch.type", "IN");
    }

    private int executeUpdate(String query) {
        return parametize(createQuery(query))
                .setMaxResults(values.getLimitValue())
                .setFirstResult(values.getOffsetValue())
                .executeUpdate();
    }

    private TypedQuery<T> createTypedQuery(String qlString) {
        return entityManager.createQuery(qlString, entityClass);
    }

    private Query createQuery(String qlString) {
        return entityManager.createQuery(qlString);
    }

    private void buildDistinct(SelectManager arel) {
        arel.distinct(values.isDistinctValue());
    }

    private void buildConstructor(SelectManager arel) {
        if (values.isConstructor()) arel.constructor(entityClass);
    }

    private void buildSelect(SelectManager arel) {
        if (values.getSelectValues().isEmpty()) {
            arel.project(getAlias());
        } else {
            values.getSelectValues().forEach(arel::project);
        }
    }

    private void buildFrom(SelectManager arel) {
        if (values.getFromClause() != null) arel.from(values.getFromClause());
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

    private boolean isValidRelationForUpdate() {
        return values.isDistinctValue() == false
                && values.getGroupValues().isEmpty()
                && values.getHavingValues().isEmpty()
                && values.getJoinsValues().isEmpty();
    }

}
