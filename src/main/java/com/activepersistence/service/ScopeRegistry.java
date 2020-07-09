package com.activepersistence.service;

public class ScopeRegistry {

    public static ThreadLocal<Relation> currentScopeRegistry = new ThreadLocal();

    public static ThreadLocal<Boolean> ignoreDefaultRegistry = ThreadLocal.withInitial(() -> false);

    public enum ValidScopeTypes { CURRENT_SCOPE, IGNORE_DEFAULT_SCOPE }

    public static Object valueFor(ValidScopeTypes scopeType) {
        switch (scopeType) {
            case CURRENT_SCOPE:
                return currentScopeRegistry.get();
            case IGNORE_DEFAULT_SCOPE:
                return ignoreDefaultRegistry.get();
            default:
                throw new RuntimeException("Scope not supported: " + scopeType);
        }
    }

    public static void setValueFor(ValidScopeTypes scopeType, Object value) {
        switch (scopeType) {
            case CURRENT_SCOPE:
                currentScopeRegistry.set((Relation) value);
                break;
            case IGNORE_DEFAULT_SCOPE:
                ignoreDefaultRegistry.set((Boolean) value);
                break;
            default:
                throw new RuntimeException("Scope not supported: " + scopeType);
        }
    }

}
