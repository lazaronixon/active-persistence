package com.activepersistence.service.relation;

import com.activepersistence.service.Relation;
import java.util.List;

public interface FinderMethods<T> {

    public Relation<T> thiz();

    public default T take() {
        return thiz().limit(1).fetchOne();
    }

    public default T takeOrFail() {
        return thiz().limit(1).fetchOneOrFail();
    }

    public default T first() {
        if (thiz().hasOrderValues()) {
            return thiz().take();
        } else {
            return thiz().order().take();
        }
    }

    public default T firstOrFail() {
        if (thiz().hasOrderValues()) {
            return thiz().takeOrFail();
        } else {
            return thiz().order("this.id").takeOrFail();
        }
    }

    public default List<T> first(int limit) {
        if (thiz().hasOrderValues()) {
            return thiz().take(limit);
        } else {
            return thiz().order("this.id").take(limit);
        }
    }

    public default T last() {
        if (thiz().hasOrderValues()) {
            return thiz().take();
        } else {
            return thiz().order("this.id DESC").take();
        }
    }

    public default T lastOrFail() {
        if (thiz().hasOrderValues()) {
            return thiz().takeOrFail();
        } else {
            return thiz().order("this.id DESC").takeOrFail();
        }
    }

    public default List<T> last(int limit) {
        if (thiz().hasOrderValues()) {
            return thiz().take(limit);
        } else {
            return thiz().order("this.id DESC").take(limit);
        }
    }

    public default T find(Object id) {
        return thiz().where("this.id = :pk").bind("pk", id).takeOrFail();
    }

    public default List<T> find(List<Object> ids) {
        return thiz().where("this.id IN :pks").bind("pks", ids).fetch();
    }

    public default T findBy(String conditions) {
        return thiz().where(conditions).take();
    }

    public default T findByOrFail(String conditions, Object... params) {
        return thiz().where(conditions).takeOrFail();
    }

    public default boolean exists(String conditions) {
        return thiz().where(conditions).exists();
    }

    public default boolean exists() {
        return thiz().limit(1).fetchExists();
    }

    public default List<T> take(int limit) {
        return thiz().limit(limit).fetch();
    }
}
