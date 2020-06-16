package com.activepersistence.service.relation;

import com.activepersistence.service.Relation;
import java.util.List;

public class FinderMethods<T> {

    private final Relation<T> relation;

    public FinderMethods(Relation relation) {
        this.relation = relation;
    }

    public T take() {
        return relation.limit(1).fetchOne();
    }

    public T takeOrFail() {
        return relation.limit(1).fetchOneOrFail();
    }

    public T first() {
        if (relation.getOrderValues().isEmpty()) {
            return relation.order("this.id").take();
        } else {
            return relation.take();
        }
    }

    public T firstOrFail() {
        if (relation.getOrderValues().isEmpty()) {
            return relation.order("this.id").takeOrFail();
        } else {
            return relation.takeOrFail();
        }
    }

    public List<T> first(int limit) {
        if (relation.getOrderValues().isEmpty()) {
            return relation.order("this.id").take(limit);
        } else {
            return relation.take(limit);
        }
    }

    public T last() {
        if (relation.getOrderValues().isEmpty()) {
            return relation.order("this.id DESC").take();
        } else {
            return relation.take();
        }
    }

    public T lastOrFail() {
        if (relation.getOrderValues().isEmpty()) {
            return relation.order("this.id DESC").takeOrFail();
        } else {
            return relation.takeOrFail();
        }
    }

    public List<T> last(int limit) {
        if (relation.getOrderValues().isEmpty()) {
            return relation.order("this.id DESC").take(limit);
        } else {
            return relation.take(limit);
        }
    }

    public T findBy(String conditions, Object... params) {
        return relation.where(conditions, params).take();
    }

    public T findByOrFail(String conditions, Object... params) {
        return relation.where(conditions, params).takeOrFail();
    }

    public boolean exists(String conditions, Object... params) {
        return relation.where(conditions, params).exists();
    }

    public boolean exists() {
        return relation.limit(1).fetchExists();
    }

    public List<T> take(int limit) {
        return relation.limit(limit).fetch();
    }

}
