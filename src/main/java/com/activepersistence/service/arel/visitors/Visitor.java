package com.activepersistence.service.arel.visitors;

import com.activepersistence.service.arel.collectors.JPQLString;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public abstract class Visitor {

    public JPQLString accept(Visitable object, JPQLString collector) {
        return visit(object, collector);
    }

    public JPQLString visit(Visitable o, JPQLString collector) {
        var dispatchMethod = getMethodFor(o.getClass());
        if (collector != null) {
            return send(dispatchMethod, o, collector);
        } else {
            return send(dispatchMethod, o);
        }
    }

    private Method getMethodFor(Class klass) {
        return getMethod("visit" + klass.getSimpleName(), klass, JPQLString.class);
    }

    private Method getMethod(String methodName, Class... params) {
        try {
            return this.getClass().getMethod(methodName, params);
        } catch (NoSuchMethodException | SecurityException ex) {
            throw new RuntimeException("Cannot visit " + methodName, ex);
        }
    }

    private JPQLString send(Method method, Object... params) {
        try {
            return (JPQLString) method.invoke(this, params);
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
            throw new RuntimeException("Cannot send method " + method.getName(), ex);
        }
    }

}
