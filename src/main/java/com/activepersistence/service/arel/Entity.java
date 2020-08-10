package com.activepersistence.service.arel;

import static com.activepersistence.service.Arel.jpql;
import com.activepersistence.service.arel.visitors.ToJpql;
import com.activepersistence.service.arel.visitors.Visitable;
import com.activepersistence.service.arel.visitors.Visitor;

public class Entity implements Source {

    public static Visitor visitor = new ToJpql();

    private final Class klass;

    private final String alias;

    public Entity(Class klass, String alias) {
        this.klass = klass;
        this.alias = alias;
    }

    public SelectManager from() {
        return new SelectManager(this);
    }

    public SelectManager where(String condition) {
        return from().where(jpql(condition));
    }

    public SelectManager where(Visitable condition) {
        return from().where(condition);
    }

    public SelectManager group(String... fields) {
        return from().group(fields);
    }

    public SelectManager having(String condition) {
        return from().having(jpql(condition));
    }

    public SelectManager having(Visitable condition) {
        return from().having(condition);
    }

    public SelectManager order(String... expr) {
        return from().order(expr);
    }

    public SelectManager project(String... things) {
        return from().project(things);
    }

    public String getSimpleName() {
        return klass.getSimpleName();
    }

    public String getName() {
        return klass.getName();
    }

    public String getAlias() {
        return alias;
    }

    public Attribute get(String name) {
        return new Attribute(this, name);
    }

}
