package com.activepersistence.service.relation;

import com.activepersistence.service.Relation;
import java.util.List;
import java.util.Map;

public interface Delegation<T> {

    public Relation<T> buildRelation();

    public default Relation<T> all() {
        return buildRelation().all();
    }

    //<editor-fold defaultstate="collapsed" desc="finder methods">
    public default T take() {
        return all().take();
    }

    public default T takeOrFail() {
        return all().takeOrFail();
    }

    public default T first() {
        return all().first();
    }

    public default T firstOrFail() {
        return all().firstOrFail();
    }

    public default T last() {
        return all().last();
    }

    public default T lastOrFail() {
        return all().lastOrFail();
    }

    public default List<T> take(int limit) {
        return all().take(limit);
    }

    public default List<T> first(int limit) {
        return all().first(limit);
    }

    public default List<T> last(int limit) {
        return all().last(limit);
    }

    public default T findBy(String conditions, Object... params) {
        return all().findBy(conditions, params);
    }

    public default T findByOrFail(String conditions, Object... params) {
        return all().findByOrFail(conditions, params);
    }

    public default boolean exists() {
        return all().exists();
    }

    public default boolean exists(String conditions, Map<String, Object> params) {
        return all().exists(conditions, params);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="query methods">
    public default Relation<T> where(String conditions, Object... params) {
        return all().where(conditions, params);
    }

    public default Relation<T> order(String... values) {
        return all().order(values);
    }

    public default Relation<T> limit(int value) {
        return all().limit(value);
    }

    public default Relation<T> offset(int value) {
        return all().offset(value);
    }

    public default Relation<T> select(String... values) {
        return all().select(values);
    }

    public default Relation<T> joins(String... values) {
        return all().joins(values);
    }

    public default Relation<T> group(String... values) {
        return all().group(values);
    }

    public default Relation<T> having(String conditions, Object... params) {
        return all().having(conditions, params);
    }

    public default Relation<T> distinct() {
        return all().distinct(true);
    }

    public default Relation<T> distinct(boolean value) {
        return all().distinct(value);
    }

    public default Relation<T> none() {
        return all().none();
    }

    public default Relation<T> includes(String... values) {
        return all().includes(values);
    }

    public default Relation<T> eagerLoads(String... values) {
        return all().eagerLoads(values);
    }

    public default Relation<T> unscope(ValidUnscopingValues... values) {
        return all().unscope(values);
    }

    public default Relation<T> reselect(String... values) {
        return all().reselect(values);
    }

    public default Relation<T> rewhere(String condition, Object... params) {
        return all().rewhere(condition, params);
    }

    public default Relation<T> reorder(String... fields) {
        return all().reorder(fields);
    }

    public default Relation<T> lock() {
        return all().lock();
    }

    public default Relation<T> from(String value) {
        return all().from(value);
    }

    public default Relation<T> scoping(Relation<T> relation) {
        return all().scoping(relation);
    }

    public default Relation<T> unscoped() {
        return all().unscoped();
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="calculation">
    public default long count() {
        return all().count();
    }

    public default long count(String field) {
        return all().count(field);
    }

    public default Object minimum(String field) {
        return all().minimum(field);
    }

    public default Object maximum(String field) {
        return all().maximum(field);
    }

    public default Object average(String field) {
        return all().average(field);
    }

    public default Object sum(String field) {
        return all().sum(field);
    }

    public default List pluck(String... field) {
        return all().pluck(field);
    }

    public default List ids() {
        return all().ids();
    }
    //</editor-fold>

}