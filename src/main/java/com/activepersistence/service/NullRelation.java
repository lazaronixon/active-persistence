package com.activepersistence.service;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.NoResultException;

public class NullRelation<T> extends Relation<T> {

    public NullRelation(Base service) {
        super(service);
    }

    @Override
    public String toJpql() {
        return "";
    }

    @Override
    public List<T> fetch() {
        return new ArrayList();
    }

    @Override
    public List fetch_() {
        return new ArrayList();
    }

    @Override
    public T fetchOne() {
        return null;
    }

    @Override
    public T fetchOneOrFail() {
        throw new NoResultException();
    }

    @Override
    public boolean fetchExists() {
        return false;
    }

    @Override
    public long count() {
        return 0;
    }

}
