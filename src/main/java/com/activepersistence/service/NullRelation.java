package com.activepersistence.service;

import static com.activepersistence.service.relation.Calculation.Operations.AVG;
import static com.activepersistence.service.relation.Calculation.Operations.COUNT;
import static com.activepersistence.service.relation.Calculation.Operations.MAX;
import static com.activepersistence.service.relation.Calculation.Operations.MIN;
import static com.activepersistence.service.relation.Calculation.Operations.SUM;
import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static java.util.Collections.emptyMap;
import java.util.List;

public class NullRelation<T, ID> extends Relation<T, ID> {

    public NullRelation(Relation<T, ID> other) {
        super(other);
    }

    @Override
    public List pluck(String... fields) {
        return emptyList();
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
            return getValues().getGroup().isEmpty() ? 0 : emptyMap();
        } else if (asList(AVG, MIN, MAX).contains(operation)) {
            return getValues().getGroup().isEmpty() ? null : emptyMap();
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
        return emptyList();
    }

}
