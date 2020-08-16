package com.activepersistence.service;

import static com.activepersistence.service.relation.Calculation.Operations.AVG;
import static com.activepersistence.service.relation.Calculation.Operations.COUNT;
import static com.activepersistence.service.relation.Calculation.Operations.MAX;
import static com.activepersistence.service.relation.Calculation.Operations.MIN;
import static com.activepersistence.service.relation.Calculation.Operations.SUM;
import java.util.ArrayList;
import static java.util.Arrays.asList;
import java.util.HashMap;
import java.util.List;

public class NullRelation<T> extends Relation<T> {

    public NullRelation(Relation<T> other) {
        super(other);
    }

    @Override
    public List pluck(String... fields) {
        return new ArrayList();
    }

    @Override
    public int deleteAll() {
        return 0;
    }

    @Override
    public int updateAll(String updates) {
        return 0;
    }

    @Override
    public String toJpql() {
        return "";
    }

    @Override
    public Object calculate(Operations operation, String field) {
        if (asList(COUNT, SUM).contains(operation)) {
            return getValues().getGroup().isEmpty() ? 0 : new HashMap();
        } else if (asList(AVG, MIN, MAX).contains(operation)) {
            return getValues().getGroup().isEmpty() ? null : new HashMap();
        } else {
            throw new RuntimeException("Operation not supported: " + operation);
        }
    }

    @Override
    public boolean exists() {
        return false;
    }

    @Override
    public boolean exists(String conditions, Object... params) {
        return false;
    }

    @Override
    public List<T> fetch() {
        return new ArrayList();
    }

}
