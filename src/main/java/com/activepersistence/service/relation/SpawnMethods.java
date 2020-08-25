package com.activepersistence.service.relation;

import com.activepersistence.service.Base;
import com.activepersistence.service.Relation;

public interface SpawnMethods<T, ID> {

    public Base<T, ID> getService();

    public Values getValues();

    public default Relation<T, ID> spawn() {
        return new Relation(thiz());
    }

    public default Relation<T, ID> merge(Relation other) {
        return spawn().merge$(other);
    }

    public default Relation<T, ID> merge$(Relation other) {
        return new Merger(thiz(), other).merge();
    }

    public default Relation<T, ID> except(ValueMethods... skips) {
        return relationWith(getValues().except(skips));
    }

    public default Relation<T, ID> only(ValueMethods... onlies) {
        return relationWith(getValues().slice(onlies));
    }

    private Relation<T, ID> relationWith(Values values) {
        return new Relation(getService(), values);
    }

    private Relation<T, ID> thiz() {
        return (Relation<T, ID>) this;
    }

}
