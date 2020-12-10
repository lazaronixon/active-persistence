package com.activepersistence.service.connectionadapters;

import com.activepersistence.service.arel.DeleteManager;
import com.activepersistence.service.arel.SelectManager;
import com.activepersistence.service.arel.UpdateManager;
import com.activepersistence.service.arel.visitors.Visitor;
import static com.activepersistence.service.connectionadapters.QueryType.JPQL;
import java.util.List;
import java.util.Map;
import javax.persistence.EntityManager;
import javax.persistence.Query;

public interface DatabaseStatements<T> {

    public Visitor getVisitor();

    public Class getEntityClass();

    public EntityManager getEntityManager();

    public default Query selectAll(String queryString, QueryType queryType) {
        if (queryType == JPQL) {
            return getEntityManager().createQuery(queryString);
        } else {
            return getEntityManager().createNativeQuery(queryString);
        }
    }

    public default List selectAll(SelectManager arel) {
        return setHints(createQuery(arel), arel.getHints()).getResultList();
    }

    public default int update(UpdateManager arel) {
        return createQuery(arel).executeUpdate();
    }

    public default int delete(DeleteManager arel) {
        return createQuery(arel).executeUpdate();
    }

    public default String toJpql(SelectManager arel) {
        return getVisitor().compile(arel.getAst(), new StringBuilder());
    }

    private Query createQuery(SelectManager arel) {
        return getEntityManager().createQuery(arel.toJpql()).setLockMode(arel.getLock()).setFirstResult(arel.getOffset()).setMaxResults(arel.getLimit());
    }

    private Query createQuery(UpdateManager arel) {
        return getEntityManager().createQuery(arel.toJpql()).setFirstResult(arel.getOffset()).setMaxResults(arel.getLimit());
    }

    private Query createQuery(DeleteManager arel) {
        return getEntityManager().createQuery(arel.toJpql()).setFirstResult(arel.getOffset()).setMaxResults(arel.getLimit());
    }

    private Query setHints(Query query, Map<String, Object> hints) {
        hints.forEach(query::setHint); return query;
    }
}
