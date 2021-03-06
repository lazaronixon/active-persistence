package com.activepersistence.repository;

import static com.activepersistence.repository.relation.Calculation.Operations.AVG;
import static com.activepersistence.repository.relation.Calculation.Operations.COUNT;
import static com.activepersistence.repository.relation.Calculation.Operations.MAX;
import static com.activepersistence.repository.relation.Calculation.Operations.MIN;
import static com.activepersistence.repository.relation.Calculation.Operations.SUM;
import java.util.ArrayList;
import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static java.util.Collections.emptyMap;
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
    public boolean isEmpty() {
        return true;
    }

    @Override
    public String toJpql() {
        return "";
    }

    @Override
    public Object calculate(Operations operation, String field) {
        if (asList(COUNT, SUM).contains(operation)) {
            return getValues().getGroup().isEmpty() ? 0L : emptyMap();
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
    public List execQueries() {
        return emptyList();
    }

}
