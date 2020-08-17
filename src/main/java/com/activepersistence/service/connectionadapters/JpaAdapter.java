package com.activepersistence.service.connectionadapters;

import javax.persistence.EntityManager;

public class JpaAdapter<T> implements Literalizing<T>, DatabaseStatements<T> {

    private final EntityManager entityManager;

    private final Class entityClass;

    public JpaAdapter(EntityManager entityManager, Class entityClass) {
        this.entityManager = entityManager;
        this.entityClass = entityClass;
    }

    @Override
    public EntityManager getEntityManager() {
        return entityManager;
    }

    @Override
    public Class getEntityClass() {
        return entityClass;
    }

}
