package com.activepersistence.service.relation;

import com.activepersistence.service.Base;
import com.activepersistence.service.Relation;
import static java.util.Arrays.asList;

public interface SpawnMethods<T> {

    public Base<T> getService();

    public Values getValues();

    public Relation<T> thiz();

    public default Relation<T> spawn() {
        return new Relation(thiz());
    }

    public default Relation<T> merge(Relation other) {
        return spawn().merge$(other);
    }

    public default Relation<T> merge$(Relation other) {
        return new Merger(thiz(), other).merge();
    }

    public default Relation<T> except(String... skips) {
        return relationWith(getValues().except(asList(skips)));
    }

    public default Relation<T> only(String... onlies) {
        return relationWith(getValues().slice(asList(onlies)));
    }

    private Relation<T> relationWith(Values values) {
        return new Relation(getService(), values);
    }

}
