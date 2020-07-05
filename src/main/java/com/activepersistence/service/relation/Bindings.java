package com.activepersistence.service.relation;

import com.activepersistence.model.Base;
import com.activepersistence.service.Relation;

public interface Bindings<T> {

    public Relation<T> thiz();

    public Relation<T> spawn();

    public Values getValues();

    public default Relation<T> bind(int position, Object value) {
        return spawn().bind_(position, value);
    }

    public default Relation<T> bind_(int position, Object value) {
        getValues().getOrdinalParameters().put(position, value); return thiz();
    }

    public default Relation<T> bind(String name, Object value) {
        return spawn().bind_(name, value);
    }

    public default Relation<T> bind_(String name, Object value) {
        getValues().getNamedParameters().put(name, value); return thiz();
    }

    public default Relation<T> bind(int position, Base record) {
        return bind(position, record.getId());
    }

    public default Relation<T> bind(String name, Base record) {
        return bind(name, record.getId());
    }

}
