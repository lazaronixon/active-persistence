package com.activepersistence.repository;

import com.activepersistence.ActivePersistenceError;
import com.activepersistence.repository.arel.DeleteManager;
import com.activepersistence.repository.arel.Entity;
import com.activepersistence.repository.arel.SelectManager;
import com.activepersistence.repository.arel.UpdateManager;
import com.activepersistence.repository.connectionadapters.JpaAdapter;
import com.activepersistence.repository.relation.Calculation;
import com.activepersistence.repository.relation.FinderMethods;
import com.activepersistence.repository.relation.QueryMethods;
import com.activepersistence.repository.relation.SpawnMethods;
import com.activepersistence.repository.relation.Values;
import java.util.Collection;
import static java.util.Collections.emptyList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import static java.util.Optional.ofNullable;
import java.util.function.Supplier;
import static java.util.stream.Collectors.toList;

public class Relation<T> implements List<T>, FinderMethods<T>, QueryMethods<T>, Calculation<T>, SpawnMethods<T> {

    private final Base repository;

    private final Class entityClass;

    private final Entity entity;

    private final Values values;

    private SelectManager arel;

    private List<T> records;

    private String toJpql;

    private boolean loaded;

    public Relation(Base repository, Values values) {
        this.entityClass = repository.getEntityClass();
        this.entity      = repository.getArelEntity();
        this.repository  = repository;
        this.values      = values;
        this.loaded      = false;
    }

    public Relation(Relation<T> other) {
        this.entityClass = other.entityClass;
        this.entity      = other.entity;
        this.repository  = other.repository;
        this.loaded      = other.loaded;
        this.values      = new Values(other.values);
        reset();
    }

    public Relation<T> unscoped() {
        return repository.unscoped();
    }

    public Relation<T> unscoped(Supplier<Relation> yield) {
        return repository.unscoped(yield);
    }

    public Relation<T> scoping(Supplier<Relation> yield) {
        var previous = Scoping.getCurrentScope();
        try {
            Scoping.setCurrentScope(this); return yield.get();
        } finally {
            Scoping.setCurrentScope(previous);
        }
    }

    public List<T> destroyAll() {
        return getRecords().stream().map(this::destroy).collect(toList());
    }

    public List<T> destroyBy(String conditions, Object... params) {
        return where(conditions, params).destroyAll();
    }

    public int deleteAll() {
        if (isValidRelationForUpdateOrDelete()) {
            var stmt = new DeleteManager();
            stmt.from(entity);
            stmt.limit(getArel().getLimit());
            stmt.offset(getArel().getOffset());
            stmt.setWheres(getArel().getConstraints());
            stmt.setOrders(getArel().getOrders());

            var affected = getConnection().delete(stmt);

            reset();

            return affected;
        } else {
            throw new ActivePersistenceError("deleteAll doesn't support this relation");
        }
    }

    public int deleteBy(String conditions, Object... params) {
        return where(conditions, params).deleteAll();
    }

    public int updateAll(String updates) {
        return _updateAll(updates);
    }

    public int updateAll(Map<String, Object> updates) {
        return _updateAll(updates);
    }

    public Relation<T> load() {
        if (loaded) {
            return this;
        } else {
            records = execQueries(); loaded = true; return this;
        }
    }

    public Relation<T> reload() {
        reset(); load(); return this;
    }

    public List execQueries() {
        var records = (List<com.activepersistence.model.Base>) getConnection().selectAll(getArel());

        if (getValues().isReadonly()) {
            records.forEach(r -> r.setReadOnly(true)); return records;
        } else {
            return records;
        }
    }

    public final Relation<T> reset() {
        toJpql = null; arel = null; loaded = false; records = emptyList(); return this;
    }

    public String toJpql() {
        return ofNullable(toJpql).orElseGet(() -> toJpql = getConnection().toJpql(getArel()));
    }

    @Override
    public List<T> getRecords() {
        load(); return records;
    }

    @Override
    public Class getEntityClass() {
        return entityClass;
    }

    @Override
    public Base<T> getRepository() {
        return repository;
    }

    @Override
    public Values getValues() {
        return values;
    }

    @Override
    public boolean isLoaded() {
        return loaded;
    }

    @Override
    public Relation<T> spawn() {
        return SpawnMethods.super.spawn();
    }

    @Override
    public String getPrimaryKeyAttr() {
        return repository.getPrimaryKeyAttr();
    }

    @Override
    public String getAlias() {
        return repository.getAlias();
    }

    @Override
    public JpaAdapter<T> getConnection() {
        return repository.getConnection();
    }

    @Override
    public String sanitizeJpql(String statement, Object... values) {
        return repository.sanitizeJpql(statement, values);
    }

    @Override
    public Boolean hasLimitOrOffset() {
        return values.getLimit() != 0 || values.getOffset() != 0;
    }

    @Override
    public SelectManager getArel() {
        return ofNullable(arel).orElseGet(() -> arel = buildArel());
    }

    //<editor-fold defaultstate="collapsed" desc="List Implementation">
    @Override
    public int size() {
        if (getValues().getGroup().isEmpty()) {
            return loaded ? records.size() : ((Long) count()).intValue();
        } else {
            throw new UnsupportedOperationException();
        }
    }

    @Override
    public boolean isEmpty() {
        return loaded ? records.isEmpty() : !exists();
    }

    @Override
    public boolean contains(Object o) {
        return getRecords().contains(o);
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        return getRecords().containsAll(c);
    }

    @Override
    public Object[] toArray() {
        return getRecords().toArray();
    }

    @Override
    public <T> T[] toArray(T[] a) {
        return getRecords().toArray(a);
    }

    @Override
    public T get(int index) {
        return getRecords().get(index);
    }

    @Override
    public int indexOf(Object o) {
        return getRecords().indexOf(o);
    }

    @Override
    public int lastIndexOf(Object o) {
        return getRecords().lastIndexOf(o);
    }

    @Override
    public Iterator<T> iterator() {
        return getRecords().iterator();
    }

    @Override
    public ListIterator<T> listIterator() {
        return getRecords().listIterator();
    }

    @Override
    public ListIterator<T> listIterator(int index) {
        return getRecords().listIterator(index);
    }

    @Override
    public List<T> subList(int fromIndex, int toIndex) {
        return getRecords().subList(fromIndex, toIndex);
    }

    @Override
    public T set(int index, T element) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean add(T e) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void add(int index, T element) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean remove(Object o) {
        throw new UnsupportedOperationException();
    }

    @Override
    public T remove(int index) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean addAll(Collection<? extends T> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean addAll(int index, Collection<? extends T> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void clear() {
        throw new UnsupportedOperationException();
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Private Methods">
    private SelectManager buildArel() {
        var result = new SelectManager(entity);

        result.constructor(values.isConstructor());
        result.distinct(values.isDistinct());

        buildSelect(result);
        buildFrom(result);

        values.getJoins().forEach(result::join);
        values.getWhere().forEach(result::where);
        values.getGroup().forEach(result::group);
        values.getHaving().forEach(result::having);
        values.getOrder().forEach(result::order);

        result.limit(values.getLimit());
        result.offset(values.getOffset());
        result.lock(values.getLock());
        result.setHints(hints());

        return result;
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

    private void addDefaultHints(HashMap hints) {
        hints.put("eclipselink.batch.type", "IN");
    }

    private void addBatchHints(HashMap hints) {
        values.getIncludes().forEach(value  -> hints.put("eclipselink.batch", value));
        values.getEagerLoad().forEach(value -> hints.put("eclipselink.left-join-fetch", value));
    }

    private HashMap<String, Object> hints() {
        var hints = new HashMap(); addDefaultHints(hints); addBatchHints(hints); return hints;
    }

    private int _updateAll(Object updates) {
        if (isValidRelationForUpdateOrDelete()) {
            var stmt = new UpdateManager();
            stmt.entity(entity);
            stmt.limit(getArel().getLimit());
            stmt.offset(getArel().getOffset());
            stmt.setWheres(getArel().getConstraints());
            stmt.setOrders(getArel().getOrders());

            if (updates instanceof Map) {
                stmt.set((Map) updates);
            } else {
                stmt.set((String) updates);
            }

            return getConnection().update(stmt);
        } else {
            throw new ActivePersistenceError("updateAll doesn't support this relation");
        }
    }

    private boolean isValidRelationForUpdateOrDelete() {
        return values.isDistinct() == false && values.getJoins().isEmpty() && values.getGroup().isEmpty();
    }

    private T destroy(T entity) {
        repository.destroy((com.activepersistence.model.Base) entity); return entity;
    }
    //</editor-fold>

}
