package com.activepersistence.service;

import com.activepersistence.ActivePersistenceError;
import com.activepersistence.service.arel.DeleteManager;
import com.activepersistence.service.arel.Entity;
import com.activepersistence.service.arel.SelectManager;
import com.activepersistence.service.arel.UpdateManager;
import com.activepersistence.service.relation.Calculation;
import com.activepersistence.service.relation.FinderMethods;
import com.activepersistence.service.relation.QueryMethods;
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

public class Relation<T> implements FinderMethods<T>, QueryMethods<T>, Calculation<T>, SpawnMethods<T> {

    private final EntityManager entityManager;

    private final Class<T> entityClass;

    private final Base<T> service;

    private final Entity entity;

    private final Values values;

    private SelectManager arel;

    public Relation(Base service) {
        this.entity        = new Entity(service.getEntityClass(), "this");
        this.entityManager = service.getEntityManager();
        this.entityClass   = service.getEntityClass();
        this.service       = service;
        this.values        = new Values();
    }

    public Relation(Base service, Values values) {
        this.entity        = new Entity(service.getEntityClass(), "this");
        this.entityManager = service.getEntityManager();
        this.entityClass   = service.getEntityClass();
        this.service       = service;
        this.values        = values;
    }

    public Relation(Relation<T> other) {
        this.entityManager = other.entityManager;
        this.entityClass   = other.entityClass;
        this.service       = other.service;
        this.entity        = other.entity;
        this.values        = new Values(other.values);
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

    public List fetch$() {
        return buildQuery$(toJpql()).getResultList();
    }

    public boolean fetchExists() {
        return buildQuery(toJpql()).getResultStream().findAny().isPresent();
    }

    public Relation<T> unscoped() {
        return service.unscoped();
    }

    public Relation<T> unscoped(Supplier<Relation> yield) {
        return service.unscoped(yield);
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
    public Relation<T> spawn() {
        return SpawnMethods.super.spawn();
    }

    @Override
    public Relation<T> thiz() {
        return this;
    }

    private SelectManager getArel() {
        return arel = ofNullable(arel).orElseGet(this::buildArel);
    }

    private SelectManager buildArel() {
        SelectManager result = new SelectManager(entity);

        values.getJoins().forEach(result::join);
        values.getWhere().forEach(result::where);
        values.getHaving().forEach(result::having);
        values.getGroup().forEach(result::group);
        values.getOrder().forEach(result::order);

        buildConstructor(result);
        buildDistinct(result);
        buildSelect(result);
        buildFrom(result);

        return result;
    }

    private TypedQuery<T> buildQuery(String query) {
        return parametize(createTypedQuery(query))
                .setLockMode(buildLockMode())
                .setMaxResults(values.getLimit())
                .setFirstResult(values.getOffset())
                .setHint("eclipselink.batch.type", "IN");
    }

    private Query buildQuery$(String query) {
        return parametize(createQuery(query))
                .setLockMode(buildLockMode())
                .setMaxResults(values.getLimit())
                .setFirstResult(values.getOffset())
                .setHint("eclipselink.batch.type", "IN");
    }

    private int executeUpdate(String query) {
        return parametize(createQuery(query))
                .setMaxResults(values.getLimit())
                .setFirstResult(values.getOffset())
                .executeUpdate();
    }

    private TypedQuery<T> createTypedQuery(String qlString) {
        return entityManager.createQuery(qlString, entityClass);
    }

    private Query createQuery(String qlString) {
        return entityManager.createQuery(qlString);
    }

    private void buildDistinct(SelectManager arel) {
        arel.distinct(values.isDistinct());
    }

    private void buildConstructor(SelectManager arel) {
        if (values.isConstructor()) arel.constructor(entityClass);
    }

    private void buildSelect(SelectManager arel) {
        if (values.getSelect().isEmpty()) {
            arel.project(getAlias());
        } else {
            values.getSelect().forEach(arel::project);
        }
    }

    private void buildFrom(SelectManager arel) {
        if (values.getFrom() != null) arel.from(values.getFrom());
    }

    private <R> TypedQuery<R> parametize(TypedQuery<R> query) {
        applyParams(query); applyHints(query); return query;
    }

    private Query parametize(Query query) {
        applyParams(query); applyHints(query); return query;
    }

    private void applyParams(Query query) {
        values.getBind().entrySet().forEach((bind) -> {
            if (bind.getKey() instanceof Integer) query.setParameter((Integer) bind.getKey(), bind.getValue());
            if (bind.getKey() instanceof String)  query.setParameter((String)  bind.getKey(), bind.getValue());
        });
    }

    private void applyHints(Query query) {
        values.getIncludes().forEach(value   -> query.setHint("eclipselink.batch", value));
        values.getEagerLoads().forEach(value -> query.setHint("eclipselink.left-join-fetch", value));
    }

    private LockModeType buildLockMode() {
        return values.isLock() ? PESSIMISTIC_READ : NONE;
    }

    private boolean isValidRelationForUpdate() {
        return values.isDistinct() == false
                && values.getGroup().isEmpty()
                && values.getHaving().isEmpty()
                && values.getJoins().isEmpty();
    }

}
