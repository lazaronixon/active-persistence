package com.activepersistence.service;

import static com.activepersistence.service.ScopeRegistry.ValidScopeTypes.CURRENT_SCOPE;
import static com.activepersistence.service.ScopeRegistry.ValidScopeTypes.IGNORE_DEFAULT_SCOPE;
import java.lang.reflect.Method;
import static java.util.Optional.ofNullable;
import java.util.function.Supplier;

public interface Scoping<T> {

    public Relation<T> getRelation();

    public Relation<T> defaultScope();

    public Class getRealClass();

    public default Relation<T> all() {
        if (getCurrentScope() != null) {
            return new Relation(getCurrentScope());
        } else {
            return defaultScoped();
        }
    }

    public default Relation<T> unscoped() {
        return getRelation();
    }

    public default Relation<T> unscoped(Supplier<Relation> yield) {
        return getRelation().scoping(yield);
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
        return ofNullable(buildDefaultScope()).orElse(getRelation());
    }

    private Relation<T> buildDefaultScope() {
        if (defaultScopeOverride()) {
            return evaluateDefaultScope(() -> getRelation().merge$(defaultScope()));
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

    private boolean defaultScopeOverride() {
        return Base.class != defaultScopeMethod().getDeclaringClass();
    }

    private Method defaultScopeMethod() {
        try {
            return getRealClass().getMethod("defaultScope");
        } catch (NoSuchMethodException | SecurityException ex) {
            throw new RuntimeException(ex.getMessage());
        }
    }

}
