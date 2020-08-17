package com.activepersistence.service.connectionadapters;

import com.activepersistence.service.arel.DeleteManager;
import com.activepersistence.service.arel.SelectManager;
import com.activepersistence.service.arel.UpdateManager;
import com.activepersistence.service.arel.visitors.ToJpql;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.persistence.EntityManager;
import javax.persistence.LockModeType;
import javax.persistence.Query;

public interface DatabaseStatements<T> {

    public Class<T> getEntityClass();

    public ToJpql getVisitor();

    public EntityManager getEntityManager();

    public default String toJpql(SelectManager arel) {
        return getVisitor().compile(arel.getAst(), new StringBuilder());
    }

    public default List<Map<String, Object>> selectAll(String sql) {
        return selectAll(sql, new HashMap());
    }

    public default List<Map<String, Object>> selectAll(String sql, Map<Integer, Object> binds) {
        return parametized$(getEntityManager().createNativeQuery(sql), binds).getResultList();
    }

    public default List<T> selectAll$(String sql) {
        return selectAll$(sql, new HashMap());
    }

    public default List<T> selectAll$(String sql, Map<Integer, Object> binds) {
        return parametized(getEntityManager().createNativeQuery(sql, getEntityClass()), binds).getResultList();
    }

    public default List selectAll(SelectManager arel, int firstResult, int maxResults, Map<String, Object> hints, LockModeType lockmode) {
        return parametized(getEntityManager().createQuery(arel.toJpql()), firstResult, maxResults, hints, lockmode).getResultList();
    }

    public default T selectOne(SelectManager arel, Map<String, Object> hints, LockModeType lockmode) {
        return (T) parametized(getEntityManager().createQuery(arel.toJpql(), getEntityClass()), 0, 0, hints, lockmode).getResultStream().findFirst().orElse(null);
    }

    public default T selectOne$(SelectManager arel, Map<String, Object> hints, LockModeType lockmode) {
        return (T) parametized(getEntityManager().createQuery(arel.toJpql(), getEntityClass()), 0, 0, hints, lockmode).getSingleResult();
    }

    public default boolean selectExists(SelectManager arel) {
        return parametized(getEntityManager().createQuery(arel.toJpql(), getEntityClass()), 0, 1).getResultStream().findAny().isPresent();
    }

    public default int update(UpdateManager arel, int firstResult, int maxResults) {
        return parametized(getEntityManager().createQuery(arel.toJpql()), firstResult, maxResults).executeUpdate();
    }

    public default int delete(DeleteManager arel, int firstResult, int maxResults) {
        return parametized(getEntityManager().createQuery(arel.toJpql()), firstResult, maxResults).executeUpdate();
    }

    private Query parametized(Query query, Map<Integer, Object> binds) {
        binds.forEach(query::setParameter); return query;
    }

    private Query parametized(Query query, int firstResult, int maxResults) {
        return query.setFirstResult(firstResult).setMaxResults(maxResults);
    }

    private Query parametized(Query query, int firstResult, int maxResults, Map<String, Object> hints, LockModeType lockmode) {
        hints.forEach(query::setHint); return query.setFirstResult(firstResult).setMaxResults(maxResults).setLockMode(lockmode);
    }

    private Query parametized$(Query query, Map<Integer, Object> binds) {
        binds.forEach(query::setParameter); query.setHint("eclipselink.result-type", "Map"); return query;
    }

}
