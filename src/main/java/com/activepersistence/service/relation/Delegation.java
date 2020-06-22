package com.activepersistence.service.relation;

import com.activepersistence.service.Relation;
import java.util.List;
import java.util.Map;

public interface Delegation<T> {

    public Relation<T> buildRelation();

    //<editor-fold defaultstate="collapsed" desc="finder methods">
    public default T take() {
        return buildRelation().all().take();
    }

    public default T takeOrFail() {
        return buildRelation().all().takeOrFail();
    }

    public default T first() {
        return buildRelation().all().first();
    }

    public default T firstOrFail() {
        return buildRelation().all().firstOrFail();
    }

    public default T last() {
        return buildRelation().all().last();
    }

    public default T lastOrFail() {
        return buildRelation().all().lastOrFail();
    }

    public default List<T> take(int limit) {
        return buildRelation().all().take(limit);
    }

    public default List<T> first(int limit) {
        return buildRelation().all().first(limit);
    }

    public default List<T> last(int limit) {
        return buildRelation().all().last(limit);
    }

    public default T findBy(String conditions, Object... params) {
        return buildRelation().all().findBy(conditions, params);
    }

    public default T findByOrFail(String conditions, Object... params) {
        return buildRelation().all().findByOrFail(conditions, params);
    }

    public default boolean exists() {
        return buildRelation().all().exists();
    }

    public default boolean exists(String conditions, Map<String, Object> params) {
        return buildRelation().all().exists(conditions, params);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="query methods">
    public default Relation<T> all() {
        return buildRelation().all();
    }

    public default Relation<T> where(String conditions, Object... params) {
        return buildRelation().all().where(conditions, params);
    }

    public default Relation<T> order(String... values) {
        return buildRelation().all().order(values);
    }

    public default Relation<T> limit(int value) {
        return buildRelation().all().limit(value);
    }

    public default Relation<T> offset(int value) {
        return buildRelation().all().offset(value);
    }

    public default Relation<T> select(String... values) {
        return buildRelation().all().select(values);
    }

    public default Relation<T> joins(String... values) {
        return buildRelation().all().joins(values);
    }

    public default Relation<T> group(String... values) {
        return buildRelation().all().group(values);
    }

    public default Relation<T> having(String conditions, Object... params) {
        return buildRelation().all().having(conditions, params);
    }

    public default Relation<T> distinct() {
        return buildRelation().all().distinct(true);
    }

    public default Relation<T> distinct(boolean value) {
        return buildRelation().all().distinct(value);
    }

    public default Relation<T> none() {
        return buildRelation().all().none();
    }

    public default Relation<T> includes(String... values) {
        return buildRelation().all().includes(values);
    }

    public default Relation<T> eagerLoads(String... values) {
        return buildRelation().all().eagerLoads(values);
    }

    public default Relation<T> unscope(ValidUnscopingValues... values) {
        return buildRelation().all().unscope(values);
    }

    public default Relation<T> reselect(String... values) {
        return buildRelation().all().reselect(values);
    }

    public default Relation<T> rewhere(String condition, Object... params) {
        return buildRelation().all().rewhere(condition, params);
    }

    public default Relation<T> reorder(String... fields) {
        return buildRelation().all().reorder(fields);
    }

    public default Relation<T> lock() {
        return buildRelation().all().lock();
    }

    public default Relation<T> from(String value) {
        return buildRelation().all().from(value);
    }

    public default Relation<T> scoping(Relation<T> relation) {
        return buildRelation().scoping(relation);
    }

    public default Relation<T> unscoped() {
        return buildRelation().unscoped();
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="calculation">
    public default long count() {
        return buildRelation().all().count();
    }

    public default long count(String field) {
        return buildRelation().all().count(field);
    }

    public default Object minimum(String field) {
        return buildRelation().all().minimum(field);
    }

    public default Object maximum(String field) {
        return buildRelation().all().maximum(field);
    }

    public default Object average(String field) {
        return buildRelation().all().average(field);
    }

    public default Object sum(String field) {
        return buildRelation().all().sum(field);
    }

    public default List pluck(String... field) {
        return buildRelation().all().pluck(field);
    }

    public default List ids() {
        return buildRelation().all().ids();
    }
    //</editor-fold>

}