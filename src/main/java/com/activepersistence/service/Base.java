package com.activepersistence.service;

import com.activepersistence.service.relation.Delegation;
import javax.persistence.EntityManager;

public abstract class Base<T> implements Persistence<T>, Querying<T>, Delegation<T>  {

    @Override
    public abstract EntityManager getEntityManager();

    @Override
    public abstract Class<T> getEntityClass();    
    
    @Override
    public Relation<T> buildRelation() {
        return new Relation(this);
    }

}
