package com.activepersistence.service.relation;

import com.activepersistence.service.Base;
import com.activepersistence.service.Relation;

public interface SpawnMethods<T> {

    public Base<T> getService();

    public Values getValues();

    public default Relation<T> spawn() {
        return new Relation(thiz());
    }

    public default Relation<T> merge(Relation other) {
        return spawn().merge$(other);
    }

    public default Relation<T> merge$(Relation other) {
        return new Merger(thiz(), other).merge();
    }

    public default Relation<T> except(ValueMethods... skips) {
        return relationWith(getValues().except(skips));
    }

    public default Relation<T> only(ValueMethods... onlies) {
        return relationWith(getValues().slice(onlies));
    }

    private Relation<T> relationWith(Values values) {
        return new Relation(getService(), values);
    }

    private Relation<T> thiz() {
        return (Relation<T>) this;
    }

}
