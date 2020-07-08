package com.activepersistence.service.relation;

import com.activepersistence.service.Base;
import com.activepersistence.service.Relation;
import static com.activepersistence.service.relation.ScopeRegistry.ValidScopeTypes.CURRENT_SCOPE;
import static com.activepersistence.service.relation.ScopeRegistry.ValidScopeTypes.IGNORE_DEFAULT_SCOPE;
import static java.util.Optional.ofNullable;
import java.util.function.Supplier;

public interface Scoping<T> {

    public Base<T> getService();

    public Relation<T> thiz();

    public default Relation<T> all() {
        if (getCurrentScope() != null) {
            return new Relation(getCurrentScope());
        } else {
            return defaultScoped();
        }
    }

    public default Relation<T> unscoped() {
        Relation scope = new Relation(getService()); setCurrentScope(scope); return scope;
    }

    public default Relation<T> unscoped(Supplier<Relation> yield) {
        return thiz().scoping(yield);
    }

    public static Relation getCurrentScope() {
        return (Relation) ScopeRegistry.valueFor(CURRENT_SCOPE);
    }

    public static void setCurrentScope(Relation scope) {
        ScopeRegistry.setValueFor(CURRENT_SCOPE, scope);
    }

    private static Boolean shouldIgnoreDefaultScope() {
        return (Boolean) ScopeRegistry.valueFor(IGNORE_DEFAULT_SCOPE);
    }

    private static void setIgnoreDefaultScope(boolean ignore) {
        ScopeRegistry.setValueFor(IGNORE_DEFAULT_SCOPE, ignore);
    }

    private Relation<T> defaultScoped() {
        return ofNullable(buildDefaultScope()).orElse(thiz());
    }

    private Relation<T> buildDefaultScope() {
        if (getService().useDefaultScope()) {
            return evaluateDefaultScope(() -> thiz().merge_(getService().defaultScope()));
        } else {
            return null;
        }
    }

    private Relation<T> evaluateDefaultScope(Supplier<Relation> yield) {
        if (shouldIgnoreDefaultScope()) return null;
        try {
            setIgnoreDefaultScope(true);
            return yield.get();
        } finally {
            setIgnoreDefaultScope(false);
        }
    }

}
