package com.activepersistence.service.arel;

import com.activepersistence.service.arel.visitors.Eclipselink;
import com.activepersistence.service.arel.visitors.Visitor;

public class Entity {

    public static Visitor visitor = new Eclipselink();

    private final Class klass;

    private final String alias;

    public Entity(Class klass) {
        this.klass = klass;
        this.alias = "this";
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

    public Class getKlass() {
        return klass;
    }

    public String getAlias() {
        return alias;
    }

    public String getName() {
        return klass.getName();
    }

    public String getSimpleName() {
        return klass.getSimpleName();
    }

}
