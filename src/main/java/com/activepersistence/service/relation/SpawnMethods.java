package com.activepersistence.service.relation;

import com.activepersistence.service.Relation;

public interface SpawnMethods<T> {

    public Relation<T> thiz();

    public Values getValues();

    public default Relation<T> spawn() {
        return new Relation(thiz());
    }

    public default Relation<T> merge(Relation<T> other) {
        return spawn().merge_(other);
    }

    public default Relation<T> merge_(Relation<T> other) {
        getValues().merge(other.getValues()); return thiz();
    }

}
