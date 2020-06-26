package com.activepersistence.service.arel;

import com.activepersistence.service.arel.visitors.ToJpql;
import com.activepersistence.service.arel.visitors.Visitor;

public class Entity implements Source {

    public static Visitor visitor = new ToJpql();

    private final Class klass;

    public Entity(Class klass) {
        this.klass = klass;
    }

    public SelectManager from() {
        return new SelectManager(this);
    }

    public SelectManager where(String condition) {
      return from().where(condition);
    }

    public SelectManager group(String... fields) {
      return from().group(fields);
    }

    public SelectManager having(String condition) {
      return from().having(condition);
    }

    public SelectManager order(String... expr) {
        return from().order(expr);
    }

    public SelectManager project(String... things) {
        return from().project(things);
    }

    public SelectManager join(String join) {
        return from().join(join);
    }

    public String getSimpleName() {
        return klass.getSimpleName();
    }

    @Override
    public String getClassName() {
        return klass.getName();
    }

}
