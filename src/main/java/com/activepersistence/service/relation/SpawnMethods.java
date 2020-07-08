package com.activepersistence.service.relation;

import com.activepersistence.service.Relation;
import static java.util.Arrays.asList;

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
        return new Merger(thiz(), other).merge();
    }

    public default Relation<T> except(String... skips) {
        return relationWith(getValues().except(asList(skips)));
    }

    public default Relation<T> only(String... onlies) {
        return relationWith(getValues().slice(asList(onlies)));
    }

    private Relation<T> relationWith(Values values) {
        return new Relation(thiz(), values);
    }

}
