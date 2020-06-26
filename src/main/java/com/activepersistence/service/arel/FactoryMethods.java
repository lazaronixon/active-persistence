package com.activepersistence.service.arel;

import com.activepersistence.service.arel.nodes.EntityAlias;

public interface FactoryMethods {

    public default EntityAlias createEntityAlias(Source relation, String name) {
        return new EntityAlias(relation, name);
    }

}
