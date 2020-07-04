package com.activepersistence.service.relation;

import com.activepersistence.service.Base;
import com.activepersistence.service.Relation;
import static java.util.Optional.ofNullable;
import java.util.function.Supplier;

public interface Scoping<T> {

    public Relation<T> thiz();

    public Base<T> getService();

    public Relation<T> getCurrentScope();

    public Relation<T> relation();

    public default Relation<T> all() {
        if (getCurrentScope() != null) {
            return new Relation(getCurrentScope());
        } else {
            return defaultScoped();
        }
    }

    public default Relation<T> unscoped() {
        return relation();
    }

    private Relation<T> defaultScoped() {
        return ofNullable(buildDefaultScope()).orElseGet(this::relation);
    }

    private Relation<T> buildDefaultScope() {
        if (getService().useDefaultScope()) {
            return evaluateDefaultScope(() -> thiz().merge_(getService().defaultScope()));
        } else {
            return null;
        }
    }

    private Relation<T> evaluateDefaultScope(Supplier<Relation> supplier) {
        if (getService().shouldIgnoreDefaultScope()) return null;

        try {
            getService().setIgnoreDefaultScope(true);
            return supplier.get();
        } finally {
            getService().setIgnoreDefaultScope(false);
        }
    }


}
