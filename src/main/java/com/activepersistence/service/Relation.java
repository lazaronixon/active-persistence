package com.activepersistence.service;

import com.activepersistence.ActivePersistenceError;
import com.activepersistence.service.arel.DeleteManager;
import com.activepersistence.service.arel.Entity;
import com.activepersistence.service.arel.SelectManager;
import com.activepersistence.service.arel.UpdateManager;
import com.activepersistence.service.connectionadapters.JpaAdapter;
import com.activepersistence.service.relation.Calculation;
import com.activepersistence.service.relation.FinderMethods;
import com.activepersistence.service.relation.QueryMethods;
import com.activepersistence.service.relation.SpawnMethods;
import com.activepersistence.service.relation.Values;
import static java.beans.Introspector.decapitalize;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import static java.util.Optional.ofNullable;
import java.util.function.Supplier;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;
import javax.persistence.LockModeType;
import static javax.persistence.LockModeType.NONE;
import static javax.persistence.LockModeType.PESSIMISTIC_WRITE;

public class Relation<T> implements FinderMethods<T>, QueryMethods<T>, Calculation<T>, SpawnMethods<T> {

    private final JpaAdapter<T> connection;

    private final Base service;

    private final Class entityClass;

    private final Entity entity;

    private final Values values;

    private SelectManager arel;

    public Relation(Base service) {
        this.entity        = buildEntity(service.getEntityClass());
        this.connection    = service.getConnection();
        this.entityClass   = service.getEntityClass();
        this.service       = service;
        this.values        = new Values();
    }

    public Relation(Base service, Values values) {
        this.entity        = buildEntity(service.getEntityClass());
        this.connection    = service.getConnection();
        this.entityClass   = service.getEntityClass();
        this.service       = service;
        this.values        = values;
    }

    public Relation(Relation<T> other) {
        this.connection    = other.connection;
        this.entityClass   = other.entityClass;
        this.service       = other.service;
        this.entity        = other.entity;
        this.values        = new Values(other.values);
    }

    public List<T> records() {
        return connection.selectAll(getArel(), values.getOffset(), values.getLimit(), lockMode(), hints());
    }

    public List fetchAll() {
        return connection.selectAll$(getArel(), values.getOffset(), values.getLimit(), lockMode(), hints());
    }

    public T fetchOne() {
        return connection.selectOne(getArel(), values.getOffset(), lockMode(), hints());
    }

    public T fetchOne$() {
        return connection.selectOne$(getArel(), values.getOffset(), lockMode(), hints());
    }

    public boolean fetchExists() {
        return connection.selectExists(getArel(), values.getOffset(), hints());
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

    public List<T> destroyAll() {
        return records().stream().map(r -> { service.destroy(r); return r; }).collect(toList());
    }

    public List<T> destroyBy(String conditions, Object... params) {
        return where(conditions, params).destroyAll();
    }

    public int deleteAll() {
        if (isValidRelationForUpdateOrDelete()) {
            var stmt = new DeleteManager();
            stmt.from(entity);
            stmt.setWheres(getArel().getConstraints());
            stmt.setOrders(getArel().getOrders());
            return connection.delete(stmt);
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

    public Class<T> getEntityClass() {
        return entityClass;
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

        values.getJoins().forEach(result::join);
        values.getWhere().forEach(result::where);
        values.getGroup().forEach(result::group);
        values.getHaving().forEach(result::having);
        values.getOrder().forEach(result::order);

        return result;
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

    private LockModeType lockMode() {
        return values.isLock() ? PESSIMISTIC_WRITE : NONE;
    }

    private Map<String, Object> hints() {
        var hints = new HashMap(); addDefaultHints(hints); addBatchHints(hints); return hints;
    }

    private void addDefaultHints(HashMap hints) {
        hints.put("eclipselink.read-only", values.isReadonly());
        hints.put("eclipselink.batch.type", "IN");
    }

    private void addBatchHints(HashMap hints) {
        values.getIncludes().forEach(value  -> hints.put("eclipselink.batch", value));
        values.getEagerLoad().forEach(value -> hints.put("eclipselink.left-join-fetch", value));
    }

    private Entity buildEntity(Class entityClass) {
        return new Entity(entityClass, decapitalize(entityClass.getSimpleName()));
    }

    private int _updateAll(Object updates) {
        if (isValidRelationForUpdateOrDelete()) {
            var stmt = new UpdateManager();
            stmt.entity(entity);
            stmt.setWheres(getArel().getConstraints());
            stmt.setOrders(getArel().getOrders());

            if (updates instanceof Map) {
                stmt.set(substituteValues((Map) updates));
            } else {
                stmt.set((String) updates);
            }

            return connection.update(stmt);
        } else {
            throw new ActivePersistenceError("updateAll doesn't support this relation");
        }
    }

    private boolean isValidRelationForUpdateOrDelete() {
        return values.isDistinct() == false
                && values.getLimit() == 0
                && values.getOffset() == 0
                && values.getGroup().isEmpty()
                && values.getHaving().isEmpty()
                && values.getJoins().isEmpty();
    }

    private Map<String, Object> substituteValues(Map<String, Object> updates) {
        return updates.entrySet().stream().collect(toMap(Entry::getKey, v -> v.getValue()));
    }

}
