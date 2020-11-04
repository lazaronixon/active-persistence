package com.activepersistence.service;

import com.activepersistence.ActivePersistenceError;
import com.activepersistence.service.arel.DeleteManager;
import com.activepersistence.service.arel.Entity;
import com.activepersistence.service.arel.SelectManager;
import com.activepersistence.service.arel.UpdateManager;
import com.activepersistence.service.connectionadapters.JpaAdapter;
import com.activepersistence.service.relation.Calculation;
import com.activepersistence.service.relation.FinderMethods;
import com.activepersistence.service.relation.PredicateBuilder;
import com.activepersistence.service.relation.QueryMethods;
import com.activepersistence.service.relation.SpawnMethods;
import com.activepersistence.service.relation.Values;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Supplier;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;

public class Relation<T, ID> implements FinderMethods<T, ID>, QueryMethods<T, ID>, Calculation<T, ID>, SpawnMethods<T, ID> {

    private final Base service;

    private final Class entityClass;

    private final Entity entity;

    private final Values values;

    public Relation(Base service) {
        this.entity        = service.getArelEntity();
        this.entityClass   = service.getEntityClass();
        this.service       = service;
        this.values        = new Values();
    }

    public Relation(Base service, Values values) {
        this.entity        = service.getArelEntity();
        this.entityClass   = service.getEntityClass();
        this.service       = service;
        this.values        = values;
    }

    public Relation(Relation<T, ID> other) {
        this.entity        = other.entity;
        this.entityClass   = other.entityClass;
        this.service       = other.service;
        this.values        = new Values(other.values);
    }

    public Relation<T, ID> unscoped() {
        return service.unscoped();
    }

    public Relation<T, ID> unscoped(Supplier<Relation> yield) {
        return service.unscoped(yield);
    }

    public Relation<T, ID> scoping(Supplier<Relation> yield) {
        var previous = Scoping.getCurrentScope();
        try {
            Scoping.setCurrentScope(this);
            return yield.get();
        } finally {
            Scoping.setCurrentScope(previous);
        }
    }

    public List<T> destroyAll() {
        return fetch().stream().map(r -> { service.destroy((com.activepersistence.model.Base) r); return r; }).collect(toList());
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
            return getConnection().delete(stmt, values.getOffset(), values.getLimit());
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
        return getConnection().toJpql(getArel());
    }

    public Class getEntityClass() {
        return entityClass;
    }

    @Override
    public Values getValues() {
        return values;
    }

    @Override
    public Base<T, ID> getService() {
        return service;
    }

    @Override
    public Relation<T, ID> spawn() {
        return SpawnMethods.super.spawn();
    }

    @Override
    public String getPrimaryKeyAttr() {
        return service.getPrimaryKeyAttr();
    }

    @Override
    public String getAlias() {
        return service.getAlias();
    }

    public JpaAdapter<T> getConnection() {
        return service.getConnection();
    }

    @Override
    public PredicateBuilder getPredicateBuilder() {
        return service.getPredicateBuilder();
    }

    public SelectManager getArel() {
        var result = new SelectManager(entity);

        buildWhere(result);
        buildHaving(result);

        buildConstructor(result);
        buildDistinct(result);
        buildSelect(result);
        buildFrom(result);

        values.getJoins().forEach(result::join);
        values.getGroup().forEach(result::group);
        values.getOrder().forEach(result::order);

        return result;
    }

    public List<T> fetch() {
        return getConnection().selectAll(getArel(), values.getOffset(), values.getLimit(), values.getLock(), hints());
    }

    public List fetch$() {
        return getConnection().selectAll(getArel(), values.getOffset(), values.getLimit(), values.getLock(), hints());
    }

    public T fetchOne() {
        return (T) getConnection().selectOne(getArel(), values.getLock(), hints());
    }

    public T fetchOne$() {
        return (T) getConnection().selectOne$(getArel(), values.getLock(), hints());
    }

    public boolean fetchExists() {
        return getConnection().selectExists(getArel());
    }

    private void buildWhere(SelectManager arel) {
        if (!values.getWhere().isEmpty()) arel.where(values.getWhere().getAst());
    }

    private void buildHaving(SelectManager arel) {
        if (!values.getHaving().isEmpty()) arel.having(values.getHaving().getAst());
    }

    private void buildDistinct(SelectManager arel) {
        arel.distinct(values.isDistinct());
    }

    private void buildConstructor(SelectManager arel) {
        arel.constructor(values.isConstructor());
    }

    private void buildSelect(SelectManager arel) {
        if (values.getSelect().isEmpty()) {
            arel.project(entity.getAlias());
        } else {
            values.getSelect().forEach(arel::project);
        }
    }

    private void buildFrom(SelectManager arel) {
        if (values.getFrom() != null) arel.from(values.getFrom());
    }

    private Map<String, Object> hints() {
        var hints = new HashMap(); addDefaultHints(hints); addBatchHints(hints); return hints;
    }

    private void addDefaultHints(HashMap hints) {
        hints.put("eclipselink.batch.type", "IN");
    }

    private void addBatchHints(HashMap hints) {
        values.getIncludes().forEach(value  -> hints.put("eclipselink.batch", value));
        values.getEagerLoad().forEach(value -> hints.put("eclipselink.left-join-fetch", value));
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

            return getConnection().update(stmt, values.getOffset(), values.getLimit());
        } else {
            throw new ActivePersistenceError("updateAll doesn't support this relation");
        }
    }

    private boolean isValidRelationForUpdateOrDelete() {
        return values.isDistinct() == false && values.getJoins().isEmpty() && values.getGroup().isEmpty() && values.getHaving().isEmpty();
    }

    private Map<String, Object> substituteValues(Map<String, Object> updates) {
        return updates.entrySet().stream().collect(toMap(Entry::getKey, v -> v.getValue()));
    }

}
