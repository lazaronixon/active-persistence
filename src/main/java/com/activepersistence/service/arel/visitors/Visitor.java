package com.activepersistence.service.arel.visitors;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public abstract class Visitor {
    
    public abstract String compile(Visitable node, StringBuilder collector);

    public StringBuilder accept(Visitable object, StringBuilder collector) {
        return visit(object, collector);
    }

    public StringBuilder visit(Visitable o, StringBuilder collector) {
        var dispatchMethod = getMethodFor(o.getClass());
        if (collector != null) {
            return send(dispatchMethod, o, collector);
        } else {
            return send(dispatchMethod, o);
        }
    }

    private Method getMethodFor(Class klass) {
        return getMethod("visit" + klass.getSimpleName(), klass, StringBuilder.class);
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

}
