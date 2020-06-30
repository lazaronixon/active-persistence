package com.activepersistence.service.arel;

import com.activepersistence.service.arel.nodes.Avg;
import com.activepersistence.service.arel.nodes.Count;
import com.activepersistence.service.arel.nodes.Max;
import com.activepersistence.service.arel.nodes.Min;
import com.activepersistence.service.arel.nodes.SqlLiteral;
import com.activepersistence.service.arel.nodes.Sum;

public interface Expressions {

    public SqlLiteral thiz();

    public default Count count() {
        return new Count(thiz());
    }

    public default Count count(boolean distinct) {
        return new Count(thiz(), distinct);
    }

    public default Sum sum() {
        return new Sum(thiz());
    }

    public default Max maximum() {
        return new Max(thiz());
    }

    public default Min minimum() {
        return new Min(thiz());
    }

    public default Avg average() {
        return new Avg(thiz());
    }

}
