package com.activepersistence.service;

import com.activepersistence.service.relation.ValueMethods;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.persistence.EntityManager;
import javax.persistence.Query;

public interface Querying<T> {

    public EntityManager getEntityManager();

    public Class<T> getEntityClass();

    public Relation<T> getRelation();

    public Relation<T> all();

    //<editor-fold defaultstate="collapsed" desc="SpawnMethods">
    public default Relation<T> merge(Relation other) {
        return all().merge(other);
    }

    public default Relation<T> except(ValueMethods... skips) {
        return all().except(skips);
    }

    public default Relation<T> only(ValueMethods... onlies) {
        return all().only(onlies);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Calculation">
    public default Object count() {
        return all().count();
    }

    public default Object count(String field) {
        return all().count(field);
    }

    public default Object minimum(String field) {
        return all().minimum(field);
    }

    public default Object maximum(String field) {
        return all().maximum(field);
    }

    public default Object average(String field) {
        return all().average(field);
    }

    public default Object sum(String field) {
        return all().sum(field);
    }

    public default List pluck(String... field) {
        return all().pluck(field);
    }

    public default List<Object> ids() {
        return all().ids();
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="FinderMethods">
    public default T take() {
        return all().take();
    }

    public default T takeOrFail() {
        return all().takeOrFail();
    }

    public default T first() {
        return all().first();
    }

    public default T firstOrFail() {
        return all().firstOrFail();
    }

    public default T last() {
        return all().last();
    }

    public default T lastOrFail() {
        return all().lastOrFail();
    }

    public default List<T> take(int limit) {
        return all().take(limit);
    }

    public default List<T> first(int limit) {
        return all().first(limit);
    }

    public default List<T> last(int limit) {
        return all().last(limit);
    }

    public default T find(Object id) {
        return all().find(id);
    }

    public default List<T> find(Object... ids) {
        return all().find(ids);
    }

    public default T findBy(String conditions, Object... params) {
        return all().findBy(conditions, params);
    }

    public default T findByOrFail(String conditions, Object... params) {
        return all().findByOrFail(conditions, params);
    }

    public default boolean exists() {
        return all().exists();
    }

    public default boolean exists(String conditions, Object... params) {
        return all().exists(conditions, params);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="QueryMethods">
    public default Relation<T> where(String conditions, Object... params) {
        return all().where(conditions, params);
    }

    public default Relation<T> order(String... values) {
        return all().order(values);
    }

    public default Relation<T> limit(int value) {
        return all().limit(value);
    }

    public default Relation<T> offset(int value) {
        return all().offset(value);
    }

    public default Relation<T> select(String... values) {
        return all().select(values);
    }

    public default Relation<T> joins(String value) {
        return all().joins(value);
    }

    public default Relation<T> joins(String path, String alias) {
        return all().joins(path, alias);
    }

    public default Relation<T> leftOuterJoins(String path, String alias) {
        return all().leftOuterJoins(path, alias);
    }

    public default Relation<T> group(String... values) {
        return all().group(values);
    }

    public default Relation<T> having(String conditions, Object... params) {
        return all().having(conditions, params);
    }

    public default Relation<T> distinct() {
        return all().distinct(true);
    }

    public default Relation<T> distinct(boolean value) {
        return all().distinct(value);
    }

    public default Relation<T> includes(String... values) {
        return all().includes(values);
    }

    public default Relation<T> eagerLoad(String... values) {
        return all().eagerLoad(values);
    }

    public default Relation<T> unscope(ValueMethods... values) {
        return all().unscope(values);
    }

    public default Relation<T> reselect(String... values) {
        return all().reselect(values);
    }

    public default Relation<T> rewhere(String conditions, Object... params) {
        return all().rewhere(conditions, params);
    }

    public default Relation<T> reorder(String... fields) {
        return all().reorder(fields);
    }

    public default Relation<T> lock() {
        return all().lock();
    }

    public default Relation<T> lock(boolean value) {
        return all().lock(value);
    }

    public default Relation<T> from(String from) {
        return all().from(from);
    }

    public default Relation<T> from(Relation relation) {
        return all().from(relation);
    }

    public default Relation<T> none() {
        return all().none();
    }

    public default Relation<T> readonly() {
        return all().readonly();
    }

    public default Relation<T> readonly(boolean value) {
       return all().readonly(value);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Relation">
    public default T findOrCreateBy(String conditions, Object[] params, T resource) {
        return all().findOrCreateBy(conditions, params, resource);
    }

    public default T findOrCreateBy(String conditions, T resource) {
        return findOrCreateBy(conditions, new Object[] {}, resource);
    }

    public default T findOrInitializeBy(String conditions, Object[] params, T resource) {
        return all().findOrInitializeBy(conditions, params, resource);
    }

    public default T findOrInitializeBy(String conditions, T resource) {
        return findOrInitializeBy(conditions, new Object[] {}, resource);
    }

    public default List<T> destroyAll() {
        return all().destroyAll();
    }

    public default List<T> destroyBy(String conditions, Object... params) {
        return all().destroyBy(conditions, params);
    }

    public default int deleteAll() {
        return all().deleteAll();
    }

    public default int deleteBy(String conditions, Object... params) {
        return all().deleteBy(conditions, params);
    }

    public default int updateAll(String updates) {
        return all().updateAll(updates);
    }

    public default int updateAll(Map<String, Object> updates) {
        return all().updateAll(updates);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Querying">
    public default List<T> findBySql(String sql) {
        return findBySql(sql, new HashMap());
    }

    public default List<T> findBySql(String sql, Map<Integer, Object> binds) {
        return parametize(getEntityManager().createNativeQuery(sql, getEntityClass()), binds).getResultList();
    }

    public default List<Map> selectAll(String sql) {
        return selectAll(sql, new HashMap());
    }

    public default List<Map> selectAll(String sql, Map<Integer, Object> binds) {
        return parametizeMap(getEntityManager().createNativeQuery(sql), binds).getResultList();
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Private">
    private Query parametize(Query query, Map<Integer, Object> binds) {
        applyParams(query, binds); return query;
    }

    private Query parametizeMap(Query query, Map<Integer, Object> binds) {
        parametize(query, binds); query.setHint("eclipselink.result-type", "Map"); return query;
    }

    private void applyParams(Query query, Map<Integer, Object> binds) {
        binds.entrySet().forEach(p -> query.setParameter(p.getKey(), p.getValue()));
    }
    //</editor-fold>

}
