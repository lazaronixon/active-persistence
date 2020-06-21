package com.activepersistence.service.relation;

import com.activepersistence.service.Relation;
import java.util.List;

public interface FinderMethods<T> {

    public default T take() {
        return thiz().limit(1).fetchOne();
    }

    public default T takeOrFail() {
        return thiz().limit(1).fetchOneOrFail();
    }

    public default T first() {
        if (thiz().getOrderValues().isEmpty()) {
            return thiz().order("this.id").take();
        } else {
            return thiz().take();
        }
    }

    public default T firstOrFail() {
        if (thiz().getOrderValues().isEmpty()) {
            return thiz().order("this.id").takeOrFail();
        } else {
            return thiz().takeOrFail();
        }
    }

    public default List<T> first(int limit) {
        if (thiz().getOrderValues().isEmpty()) {
            return thiz().order("this.id").take(limit);
        } else {
            return thiz().take(limit);
        }
    }

    public default T last() {
        if (thiz().getOrderValues().isEmpty()) {
            return thiz().order("this.id DESC").take();
        } else {
            return thiz().take();
        }
    }

    public default T lastOrFail() {
        if (thiz().getOrderValues().isEmpty()) {
            return thiz().order("this.id DESC").takeOrFail();
        } else {
            return thiz().takeOrFail();
        }
    }

    public default List<T> last(int limit) {
        if (thiz().getOrderValues().isEmpty()) {
            return thiz().order("this.id DESC").take(limit);
        } else {
            return thiz().take(limit);
        }
    }

    public default T findBy(String conditions, Object... params) {
        return thiz().where(conditions, params).take();
    }

    public default T findByOrFail(String conditions, Object... params) {
        return thiz().where(conditions, params).takeOrFail();
    }

    public default boolean exists(String conditions, Object... params) {
        return thiz().where(conditions, params).exists();
    }

    public default boolean exists() {
        return thiz().limit(1).fetchExists();
    }

    public default List<T> take(int limit) {
        return thiz().limit(limit).fetch();
    }

    private Relation<T> thiz() {
        return (Relation<T>) this;
    }

}
