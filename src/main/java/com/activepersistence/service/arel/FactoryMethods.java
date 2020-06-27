package com.activepersistence.service.arel;

import com.activepersistence.service.arel.nodes.EntityAlias;
import com.activepersistence.service.arel.nodes.Grouping;
import com.activepersistence.service.arel.nodes.SelectStatement;

public interface FactoryMethods {

    public default EntityAlias createEntityAlias(Grouping relation, String name) {
        return new EntityAlias(relation, name);
    }

    public default Grouping grouping(SelectStatement expr) {
        return new Grouping(expr);
    }

}
