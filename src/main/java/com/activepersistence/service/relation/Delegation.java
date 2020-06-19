package com.activepersistence.service.relation;

import com.activepersistence.service.Relation;
import java.util.List;
import java.util.Map;

public interface Delegation<T> {

    public Relation<T> buildRelation();

    //<editor-fold defaultstate="collapsed" desc="finder methods">
    public default T take() {
        return buildRelation().take();
    }

    public default T takeOrFail() {
        return buildRelation().takeOrFail();
    }

    public default T first() {
        return buildRelation().first();
    }

    public default T firstOrFail() {
        return buildRelation().firstOrFail();
    }

    public default T last() {
        return buildRelation().last();
    }

    public default T lastOrFail() {
        return buildRelation().lastOrFail();
    }

    public default List<T> take(int limit) {
        return buildRelation().take(limit);
    }

    public default List<T> first(int limit) {
        return buildRelation().first(limit);
    }

    public default List<T> last(int limit) {
        return buildRelation().last(limit);
    }

    public default T findBy(String conditions, Object... params) {
        return buildRelation().findBy(conditions, params);
    }

    public default T findByOrFail(String conditions, Object... params) {
        return buildRelation().findByOrFail(conditions, params);
    }

    public default boolean exists() {
        return buildRelation().exists();
    }

    public default boolean exists(String conditions, Map<String, Object> params) {
        return buildRelation().exists(conditions, params);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="query methods">
    public default Relation<T> all() {
        return buildRelation().all();
    }

    public default Relation<T> where(String conditions, Object... params) {
        return buildRelation().where(conditions, params);
    }

    public default Relation<T> order(String... values) {
        return buildRelation().order(values);
    }

    public default Relation<T> limit(int value) {
        return buildRelation().limit(value);
    }

    public default Relation<T> offset(int value) {
        return buildRelation().offset(value);
    }

    public default Relation<T> select(String... values) {
        return buildRelation().select(values);
    }

    public default Relation<T> joins(String... values) {
        return buildRelation().joins(values);
    }

    public default Relation<T> group(String... values) {
        return buildRelation().group(values);
    }

    public default Relation<T> having(String conditions, Object... params) {
        return buildRelation().having(conditions, params);
    }

    public default Relation<T> distinct() {
        return buildRelation().distinct();
    }

    public default Relation<T> none() {
        return buildRelation().none();
    }

    public default Relation<T> includes(String... values) {
        return buildRelation().includes(values);
    }

    public default Relation<T> eagerLoads(String... values) {
        return buildRelation().eagerLoads(values);
    }

    public default Relation<T> unscope(ValidUnscopingValues... values) {
        return buildRelation().unscope(values);
    }

    public default Relation<T> reselect(String... values) {
        return buildRelation().reselect(values);
    }

    public default Relation<T> rewhere(String condition, Object... params) {
        return buildRelation().rewhere(condition, params);
    }

    public default Relation<T> reorder(String... fields) {
        return buildRelation().reorder(fields);
    }

    public default Relation<T> lock() {
        return buildRelation().lock();
    }

    public default Relation<T> from(String value) {
        return buildRelation().from(value);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="calculation">
    public default long count() {
        return buildRelation().count();
    }

    public default long count(String field) {
        return buildRelation().count(field);
    }

    public default <R> R minimum(String field, Class<R> resultClass) {
        return buildRelation().minimum(field, resultClass);
    }

    public default <R> R maximum(String field, Class<R> resultClass) {
        return buildRelation().maximum(field, resultClass);
    }

    public default <R> R average(String field, Class<R> resultClass) {
        return buildRelation().average(field, resultClass);
    }

    public default <R> R sum(String field, Class<R> resultClass) {
        return buildRelation().sum(field, resultClass);
    }

    public default List pluck(String... field) {
        return buildRelation().pluck(field);
    }

    public default List ids() {
        return buildRelation().ids();
    }
    //</editor-fold>

}