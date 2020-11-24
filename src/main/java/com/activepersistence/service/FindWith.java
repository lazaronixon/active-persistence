package com.activepersistence.service;

import static java.util.Optional.ofNullable;
import java.util.function.Supplier;


public class FindWith<T> {

    private final String conditions;

    private final Object[] params;

    private final Relation<T> relation;

    private final Base<T> service;

    public FindWith(String conditions, Object[] params, Relation relation) {
        this.conditions = conditions;
        this.params     = params;
        this.relation   = relation;
        this.service    = relation.getService();
    }

    public T orCreate(T other) {
        return ofNullable(relation.findBy(conditions, params)).orElseGet(() -> service._createRecord(other));
    }

    public T orCreate(Supplier<? extends T> supplier) {
        return ofNullable(relation.findBy(conditions, params)).orElseGet(() -> service._createRecord(supplier.get()));
    }

    public T orGet(T other) {
        return ofNullable(relation.findBy(conditions, params)).orElseGet(() -> other);
    }

    public T orGet(Supplier<? extends T> supplier) {
        return ofNullable(relation.findBy(conditions, params)).orElseGet(() -> supplier.get());
    }

}
