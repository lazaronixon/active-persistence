package com.activepersistence.repository;

import com.activepersistence.repository.arel.Entity;
import com.activepersistence.repository.relation.Values;
import static java.beans.Introspector.decapitalize;

public interface Core<T> {

    public Class getEntityClass();

    public default String getPrimaryKey() {
        return "id";
    }

    public default String getAlias() {
        return decapitalize(getEntityClass().getSimpleName());
    }

    public default Entity getArelEntity() {
        return new Entity(getEntityClass(), getAlias());
    }

    public default Relation<T> getRelation() {
        return new Relation(((Base) this), new Values());
    }

    public default String getPrimaryKeyAttr() {
        return getAlias() + "." + getPrimaryKey();
    }

}
