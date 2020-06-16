package com.activepersistence.service;

import com.activepersistence.service.relation.Delegation;
import javax.persistence.EntityManager;

public abstract class Base<T> implements Persistence<T>, Querying<T>, Delegation<T>  {

    private final Class<T> entityClass;

    public Base(Class<T> entityClass) { this.entityClass = entityClass; }

    @Override
    public abstract EntityManager getEntityManager();

    @Override
    public Class<T> getEntityClass() { return entityClass; }

    @Override
    public Relation<T> buildRelation() { return new Relation(getEntityManager(), getEntityClass()); }

}
