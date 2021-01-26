package com.activepersistence.repository.relation;

import com.activepersistence.repository.Base;
import com.activepersistence.repository.Relation;

public interface SpawnMethods<T> {

    public Base<T> getRepository();

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
        return new Relation(getRepository(), values);
    }

    private Relation<T> thiz() {
        return (Relation<T>) this;
    }

}
