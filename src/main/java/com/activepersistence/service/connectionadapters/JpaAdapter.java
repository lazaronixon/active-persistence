package com.activepersistence.service.connectionadapters;

import com.activepersistence.service.arel.visitors.ToJpql;
import com.activepersistence.service.arel.visitors.Visitor;
import javax.persistence.EntityManager;

public class JpaAdapter<T> implements DatabaseStatements<T> {

    private final EntityManager entityManager;

    private final Class entityClass;

    private final Visitor visitor;

    public JpaAdapter(EntityManager entityManager, Class entityClass) {
        this.entityManager = entityManager;
        this.entityClass   = entityClass;
        this.visitor       = new ToJpql();
    }

    @Override
    public EntityManager getEntityManager() {
        return entityManager;
    }

    @Override
    public Class getEntityClass() {
        return entityClass;
    }

    @Override
    public Visitor getVisitor() {
        return visitor;
    }

}
