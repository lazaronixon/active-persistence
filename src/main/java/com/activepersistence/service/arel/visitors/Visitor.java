package com.activepersistence.service.arel.visitors;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public abstract class Visitor {

    public StringBuilder accept(Object object, StringBuilder collector) {
        return visit(object, collector);
    }

    public StringBuilder visit(Object o, StringBuilder collector) {
        Method dispatchMethod = getMethodFor(o.getClass());
        if (collector != null) {
            return send(dispatchMethod, o, collector);
        } else {
            return send(dispatchMethod, o);
        }
    }

    private Method getMethodFor(Class klass) {
        return getMethod("visit" + capitalize(klass.getSimpleName()), klass, StringBuilder.class);
    }

    private Method getMethod(String methodName, Class... params) {
        try {
            return this.getClass().getMethod(methodName, params);
        } catch (NoSuchMethodException | SecurityException ex) {
            throw new RuntimeException("Cannot visit " + methodName, ex);
        }
    }

    private StringBuilder send(Method method, Object... params) {
        try {
            return (StringBuilder) method.invoke(this, params);
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
            throw new RuntimeException("Cannot send method " + method.getName(), ex);
        }
    }

    private String capitalize(String s) {
        return s.substring(0, 1).toUpperCase() + s.substring(1);
    }

}
