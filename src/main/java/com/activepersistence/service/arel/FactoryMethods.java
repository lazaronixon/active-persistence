package com.activepersistence.service.arel;

import com.activepersistence.service.arel.nodes.EntityAlias;
import com.activepersistence.service.arel.nodes.Grouping;
import com.activepersistence.service.arel.nodes.SelectStatement;
import com.activepersistence.service.arel.nodes.SqlLiteral;
import com.activepersistence.service.arel.nodes.StringJoin;

public interface FactoryMethods {

    public default EntityAlias createEntityAlias(Grouping relation, String name) {
        return new EntityAlias(relation, name);
    }

    public default StringJoin createStringJoin(SqlLiteral to) {
        return new StringJoin(to);
    }

    public default Grouping grouping(SelectStatement expr) {
        return new Grouping(expr);
    }

}
