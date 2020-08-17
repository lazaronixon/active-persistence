package com.activepersistence.service.connectionadapters;

import com.activepersistence.service.arel.DeleteManager;
import com.activepersistence.service.arel.SelectManager;
import com.activepersistence.service.arel.UpdateManager;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.persistence.EntityManager;
import javax.persistence.LockModeType;
import static javax.persistence.LockModeType.NONE;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

public interface DatabaseStatements<T> {

    public Class<T> getEntityClass();

    public EntityManager getEntityManager();

    public default List<Map<String, Object>> selectAll(String sql) {
        return selectAll(sql, new HashMap());
    }

    public default List<Map<String, Object>> selectAll(String sql, Map<Integer, Object> binds) {
        return parametizedNative(getEntityManager().createNativeQuery(sql), binds).getResultList();
    }

    public default List<T> selectAll$(String sql) {
        return selectAll$(sql, new HashMap());
    }

    public default List<T> selectAll$(String sql, Map<Integer, Object> binds) {
        return parametized(getEntityManager().createNativeQuery(sql, getEntityClass()), binds).getResultList();
    }

    public default List<T> selectAll(SelectManager arel, int firstResult, int maxResults, LockModeType lockmode, Map<String, Object> hints) {
        return parametized(getEntityManager().createQuery(arel.toJpql(), getEntityClass()), firstResult, maxResults, lockmode, hints).getResultList();
    }

    public default List selectAll$(SelectManager arel, int firstResult, int maxResults, LockModeType lockmode, Map<String, Object> hints) {
        return parametized(getEntityManager().createQuery(arel.toJpql()), firstResult, maxResults, lockmode, hints).getResultList();
    }

    public default T selectOne(SelectManager arel, int firstResult, LockModeType lockmode, Map<String, Object> hints) {
        return parametized(getEntityManager().createQuery(arel.toJpql(), getEntityClass()), firstResult, 1, lockmode, hints).getResultStream().findFirst().orElse(null);
    }

    public default T selectOne$(SelectManager arel, int firstResult, LockModeType lockmode, Map<String, Object> hints) {
        return parametized(getEntityManager().createQuery(arel.toJpql(), getEntityClass()), firstResult, 1, lockmode, hints).getSingleResult();
    }

    public default boolean selectExists(SelectManager arel, int firstResult, Map<String, Object> hints) {
        return parametized(getEntityManager().createQuery(arel.toJpql(), getEntityClass()), firstResult, 1, NONE, hints).getResultStream().findAny().isPresent();
    }

    public default int update(UpdateManager arel) {
        return getEntityManager().createQuery(arel.toJpql()).executeUpdate();
    }

    public default int delete(DeleteManager arel) {
        return getEntityManager().createQuery(arel.toJpql()).executeUpdate();
    }

    private Query parametizedNative(Query query, Map<Integer, Object> binds) {
        binds.forEach(query::setParameter); query.setHint("eclipselink.result-type", "Map"); return query;
    }

    private Query parametized(Query query, Map<Integer, Object> binds) {
        binds.forEach(query::setParameter); return query;
    }

    private Query parametized(Query query, int firstResult, int maxResults, LockModeType lockmode, Map<String, Object> hints) {
        hints.forEach(query::setHint); return query.setFirstResult(firstResult).setMaxResults(maxResults).setLockMode(lockmode);
    }

    private <R> TypedQuery<R> parametized(TypedQuery<R> query, int firstResult, int maxResults, LockModeType lockmode, Map<String, Object> hints) {
        hints.forEach(query::setHint); return query.setFirstResult(firstResult).setMaxResults(maxResults).setLockMode(lockmode);
    }

}
