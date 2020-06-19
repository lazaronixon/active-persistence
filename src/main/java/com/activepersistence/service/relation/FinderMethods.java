package com.activepersistence.service.relation;

import com.activepersistence.service.Relation;
import java.util.List;
import java.util.Map;

public interface FinderMethods<T> {

    public Relation<T> getRelation();

    public default T take() {
        return getRelation().limit(1).fetchOne();
    }

    public default T takeOrFail() {
        return getRelation().limit(1).fetchOneOrFail();
    }

    public default T first() {
        if (getRelation().getOrderValues().isEmpty()) {
            return getRelation().order("this.id").take();
        } else {
            return getRelation().take();
        }
    }

    public default T firstOrFail() {
        if (getRelation().getOrderValues().isEmpty()) {
            return getRelation().order("this.id").takeOrFail();
        } else {
            return getRelation().takeOrFail();
        }
    }

    public default List<T> first(int limit) {
        if (getRelation().getOrderValues().isEmpty()) {
            return getRelation().order("this.id").take(limit);
        } else {
            return getRelation().take(limit);
        }
    }

    public default T last() {
        if (getRelation().getOrderValues().isEmpty()) {
            return getRelation().order("this.id DESC").take();
        } else {
            return getRelation().take();
        }
    }

    public default T lastOrFail() {
        if (getRelation().getOrderValues().isEmpty()) {
            return getRelation().order("this.id DESC").takeOrFail();
        } else {
            return getRelation().takeOrFail();
        }
    }

    public default List<T> last(int limit) {
        if (getRelation().getOrderValues().isEmpty()) {
            return getRelation().order("this.id DESC").take(limit);
        } else {
            return getRelation().take(limit);
        }
    }

    public default T findBy(String conditions, Map<String, Object> params) {
        return getRelation().where(conditions, params).take();
    }

    public default T findByOrFail(String conditions, Map<String, Object> params) {
        return getRelation().where(conditions, params).takeOrFail();
    }

    public default boolean exists(String conditions, Map<String, Object> params) {
        return getRelation().where(conditions, params).exists();
    }

    public default boolean exists() {
        return getRelation().limit(1).fetchExists();
    }

    public default List<T> take(int limit) {
        return getRelation().limit(limit).fetch();
    }

}
