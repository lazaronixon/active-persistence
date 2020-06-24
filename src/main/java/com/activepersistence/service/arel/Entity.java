package com.activepersistence.service.arel;

import com.activepersistence.service.arel.visitors.Eclipselink;
import com.activepersistence.service.arel.visitors.Visitor;

public class Entity {

    public static Visitor visitor = new Eclipselink();

    private final String name;
    private final String alias;

    public Entity(String name, String alias) {
        this.name = name;
        this.alias = alias;
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

    public String getName() {
        return name;
    }

    public String getAlias() {
        return alias;
    }

}
