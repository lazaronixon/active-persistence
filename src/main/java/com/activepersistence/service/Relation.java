package com.activepersistence.service;

import com.activepersistence.ActivePersistenceError;
import com.activepersistence.service.arel.DeleteManager;
import com.activepersistence.service.arel.Entity;
import com.activepersistence.service.arel.SelectManager;
import com.activepersistence.service.arel.UpdateManager;
import com.activepersistence.service.relation.Calculation;
import com.activepersistence.service.relation.FinderMethods;
import static com.activepersistence.service.relation.Literalizing.literal;
import com.activepersistence.service.relation.QueryMethods;
import com.activepersistence.service.relation.SpawnMethods;
import com.activepersistence.service.relation.Values;
import static java.beans.Introspector.decapitalize;
import static java.util.Arrays.asList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import static java.util.Optional.ofNullable;
import java.util.function.Supplier;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;
import javax.persistence.EntityManager;
import javax.persistence.LockModeType;
import static javax.persistence.LockModeType.NONE;
import static javax.persistence.LockModeType.PESSIMISTIC_READ;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

public class Relation<T> implements FinderMethods<T>, QueryMethods<T>, Calculation<T>, SpawnMethods<T> {

    private final EntityManager entityManager;

    private final Base<T> service;

    private final Class entityClass;

    private final Entity entity;

    private final Values values;

    private SelectManager arel;

    public Relation(Base service) {
        this.entity        = buildEntity(service.getEntityClass());
        this.entityManager = service.getEntityManager();
        this.entityClass   = service.getEntityClass();
        this.service       = service;
        this.values        = new Values();
    }

    public Relation(Base service, Values values) {
        this.entity        = buildEntity(service.getEntityClass());
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

    public T fetchOne$() {
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
        var previous = Scoping.getCurrentScope();
        try {
            Scoping.setCurrentScope(thiz());
            return yield.get();
        } finally {
            Scoping.setCurrentScope(previous);
        }
    }

    public T findOrCreateBy(String conditions, Object[] params, T resource) {
        return ofNullable(findBy(conditions, params)).orElseGet(() -> service.create(resource));
    }

    public T findOrCreateBy(String conditions, T resource) {
        return findOrCreateBy(conditions, new Object[] {}, resource);
    }

    public T findOrInitializeBy(String conditions, Object[] params, T resource) {
        return ofNullable(findBy(conditions, params)).orElseGet(() -> resource);
    }

    public T findOrInitializeBy(String conditions, T resource) {
        return findOrInitializeBy(conditions, new Object[] {}, resource);
    }

    public List<T> destroyAll() {
        return fetch().stream().map(r -> { service.destroy(r); return r; }).collect(toList());
    }

    public List<T> destroyBy(String conditions, Object... params) {
        return where(conditions, params).destroyAll();
    }

    public int deleteAll() {
        if (isValidRelationForUpdate()) {
            var stmt = new DeleteManager();
            stmt.from(entity);
            stmt.setWheres(getArel().getConstraints());
            stmt.setOrders(getArel().getOrders());
            return executeUpdate(stmt.toJpql());
        } else {
            throw new ActivePersistenceError("deleteAll doesn't support this relation");
        }
    }

    public int updateAll(String updates) {
        return _updateAll(updates);
    }

    public int updateAll(Map<String, Object> updates) {
        return _updateAll(updates);
    }

    public int deleteBy(String conditions, Object... params) {
        return where(conditions, params).deleteAll();
    }

    public String toJpql() {
        return getArel().toJpql();
    }

    public Class getEntityClass() {
        return entityClass;
    }

    public EntityManager getEntityManager() {
        return entityManager;
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
        return getAlias() + "." + "id";
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
        var result = new SelectManager(entity);

        buildConstructor(result);
        buildDistinct(result);
        buildSelect(result);
        buildFrom(result);
        buildJoins(result);

        values.getWhere().forEach(result::where);
        values.getHaving().forEach(result::having);
        values.getGroup().forEach(result::group);
        values.getOrder().forEach(result::order);

        return result;
    }

    private TypedQuery<T> buildQuery(String query) {
        return parametize(createTypedQuery(query))
                .setLockMode(buildLockMode())
                .setMaxResults(values.getLimit())
                .setFirstResult(values.getOffset())
                .setHint("eclipselink.read-only", values.isReadonly())
                .setHint("eclipselink.batch.type", "IN");
    }

    private Query buildQuery$(String query) {
        return parametize(createQuery(query))
                .setLockMode(buildLockMode())
                .setMaxResults(values.getLimit())
                .setFirstResult(values.getOffset())
                .setHint("eclipselink.read-only", values.isReadonly())
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
        arel.constructor(values.isConstructor());
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

    private void buildJoins(SelectManager result) {
        values.getJoins().forEach(join -> {
            if (isJoinFragment(join)) {
                result.join(join);
            } else {
                result.join(join, fieldAlias(join));
            }
        });

        values.getLeftOuterJoins().forEach(leftOuterJoin -> {
            result.outerJoin(leftOuterJoin, fieldAlias(leftOuterJoin));
        });
    }

    private <R> TypedQuery<R> parametize(TypedQuery<R> query) {
        applyHints(query); return query;
    }

    private Query parametize(Query query) {
        applyHints(query); return query;
    }

    private void applyHints(Query query) {
        values.getIncludes().forEach(value  -> query.setHint("eclipselink.batch", value));
        values.getEagerLoad().forEach(value -> query.setHint("eclipselink.left-join-fetch", value));
    }

    private LockModeType buildLockMode() {
        return values.isLock() ? PESSIMISTIC_READ : NONE;
    }

    private Entity buildEntity(Class entityClass) {
        return new Entity(entityClass, decapitalize(entityClass.getSimpleName()));
    }

    private int _updateAll(Object updates) {
        if (isValidRelationForUpdate()) {
            var stmt = new UpdateManager();
            stmt.entity(entity);
            stmt.setWheres(getArel().getConstraints());
            stmt.setOrders(getArel().getOrders());

            if (updates instanceof Map) {
                stmt.set(substituteValues((Map) updates));
            } else {
                stmt.set((String) updates);
            }

            return executeUpdate(stmt.toJpql());
        } else {
            throw new ActivePersistenceError("updateAll doesn't support this relation");
        }
    }

    private Map<String, String> substituteValues(Map<String, Object> updates) {
        return updates.entrySet().stream().collect(toMap(Entry::getKey, v -> literal(v.getValue())));
    }

    private boolean isValidRelationForUpdate() {
        return values.isDistinct() == false
                && values.getGroup().isEmpty()
                && values.getHaving().isEmpty()
                && values.getJoins().isEmpty()
                && values.getLeftOuterJoins().isEmpty();
    }

    private boolean isJoinFragment(String join) {
        return join.startsWith("JOIN ") || join.startsWith("INNER ") || join.startsWith("LEFT ");
    }

    private String fieldAlias(String join) {
        var result = join.split("[.]"); return result[result.length -1];
    }

}
