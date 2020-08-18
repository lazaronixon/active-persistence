package com.activepersistence.service;

import com.activepersistence.service.arel.Entity;
import com.activepersistence.service.connectionadapters.JpaAdapter;
import static java.beans.Introspector.decapitalize;
import javax.persistence.EntityManager;

public abstract class Base<T> implements Persistence<T>, Querying<T>, Scoping<T> {

    private static final String PROXY = "$Proxy$_$$_WeldSubclass";

    private final Class entityClass;

    public Base(Class entityClass) {
        this.entityClass = entityClass;
    }

    public Class<T> getEntityClass() {
        return entityClass;
    }

    public String getAlias() {
        return decapitalize(entityClass.getSimpleName());
    }

    public Entity getArelEntity() {
        return new Entity(entityClass, getAlias());
    }

    public String getPrimaryKey() {
        return getAlias() + "." + "id";
    }

    @Override
    public abstract EntityManager getEntityManager();

    @Override
    public Relation<T> getRelation() {
        return new Relation(this);
    }

    @Override
    public JpaAdapter<T> getConnection() {
        return new JpaAdapter(getEntityManager(), entityClass);
    }

    @Override
    public Relation<T> all() {
        return Scoping.super.all();
    }

    @Override
    public Relation<T> defaultScope() {
        throw new UnsupportedOperationException("not implemented");
    }

    @Override
    public Class getRealClass() {
        return getClass().getSimpleName().contains(PROXY) ? getClass().getSuperclass() : getClass();
    }

}
