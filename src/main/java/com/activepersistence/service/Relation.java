package com.activepersistence.service;


import com.activepersistence.service.relation.Calculation;
import com.activepersistence.service.relation.FinderMethods;
import com.activepersistence.service.relation.QueryMethods;
import static java.lang.String.format;
import static java.lang.String.join;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import static java.util.Optional.ofNullable;
import static java.util.stream.IntStream.range;
import javax.persistence.EntityManager;
import static javax.persistence.LockModeType.NONE;
import static javax.persistence.LockModeType.PESSIMISTIC_READ;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

public class Relation<T> implements Querying<T> {

    private final static String SELECT     = "SELECT %s";
    private final static String FROM       = "FROM %s";
    private final static String WHERE      = "WHERE %s";
    private final static String JOIN       = "JOIN %s";
    private final static String LEFT_JOIN  = "LEFT JOIN %s";
    private final static String GROUP      = "GROUP BY %s";
    private final static String HAVING     = "HAVING %s";
    private final static String ORDER      = "ORDER BY %s";

    private final EntityManager entityManager;

    private final FinderMethods<T> finderMethods;

    private final QueryMethods<T> queryMethods;

    private final Calculation<T> calculation;

    private final Class entityClass;

    private final List<String> selectValues    = new ArrayList();

    private final List<String> whereValues     = new ArrayList();

    private final List<String> groupValues     = new ArrayList();

    private final List<String> havingValues    = new ArrayList();

    private final List<String> orderValues     = new ArrayList();

    private final List<String> joinsValues     = new ArrayList();

    private final List<String> leftJoinsValues = new ArrayList();

    private final List<Object> params = new ArrayList();

    private final List<String> includesValues = new ArrayList();

    private final List<String> eagerLoadsValues = new ArrayList();

    private String fromClause = null;

    private int limit  = 0;

    private int offset = 0;

    private boolean distinct = false;

    private boolean constructor = false;

    private boolean lock = false;

    public Relation(EntityManager entityManager, Class entityClass) {
        this.entityManager = entityManager;
        this.entityClass   = entityClass;
        this.finderMethods = new FinderMethods(this);
        this.queryMethods  = new QueryMethods(this);
        this.calculation   = new Calculation(this);
    }

    @Override
    public EntityManager getEntityManager() {
        return entityManager;
    }

    @Override
    public Class<T> getEntityClass() {
        return entityClass;
    }

    public String toJpql() {
        StringBuilder qlString = new StringBuilder(buildSelect());
        if (!joinsValues.isEmpty())     qlString.append(" ").append(buildJoins());
        if (!leftJoinsValues.isEmpty()) qlString.append(" ").append(buildLeftJoins());
        if (!whereValues.isEmpty())     qlString.append(" ").append(buildWhere());
        if (!groupValues.isEmpty())     qlString.append(" ").append(buildGroup());
        if (!havingValues.isEmpty())    qlString.append(" ").append(buildHaving());
        if (!orderValues.isEmpty())     qlString.append(" ").append(buildOrder());
        return qlString.toString();
    }

    public T find(Object id) {
        return getEntityManager().find(getEntityClass(), id, (lock ? PESSIMISTIC_READ : NONE));
    }

    //<editor-fold defaultstate="collapsed" desc="fetch methods">
    public T fetchOne() {
        return buildParameterizedQuery(toJpql()).getResultStream().findFirst().orElse(null);
    }

    public T fetchOneOrFail() {
        return buildParameterizedQuery(toJpql()).getSingleResult();
    }

    public List<T> fetch() {
        return buildParameterizedQuery(toJpql()).getResultList();
    }

    public <R> R fetchOneAs(Class<R> resultClass) {
        return buildParameterizedQuery(toJpql(), resultClass).getSingleResult();
    }

    public List fetchAlt() {
        return buildParameterizedQueryAlt(toJpql()).getResultList();
    }

    public boolean fetchExists() {
        return buildParameterizedQuery(toJpql()).getResultStream().findAny().isPresent();
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="finder methods">
    public T take() {
        return finderMethods.take();
    }

    public T takeOrFail() {
        return finderMethods.takeOrFail();
    }

    public T first() {
        return finderMethods.first();
    }

    public T firstOrFail() {
        return finderMethods.firstOrFail();
    }

    public T last() {
        return finderMethods.last();
    }

    public T lastOrFail() {
        return finderMethods.lastOrFail();
    }

    public T findBy(String conditions, Object... params) {
        return finderMethods.findBy(conditions, params);
    }

    public T findByOrFail(String conditions, Object... params) {
        return finderMethods.findByOrFail(conditions, params);
    }

    public boolean exists(String conditions, Object... params) {
        return finderMethods.exists(conditions, params);
    }

    public boolean exists() {
        return finderMethods.exists();
    }

    public List<T> take(int limit) {
        return finderMethods.take(limit);
    }

    public List<T> first(int limit) {
        return finderMethods.first(limit);
    }

    public List<T> last(int limit) {
        return finderMethods.last(limit);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="query methods">
    public Relation<T> all() {
        return queryMethods.all();
    }

    public Relation<T> select(String... fields) {
        return queryMethods.select(fields);
    }

    public Relation<T> joins(String... values) {
        return queryMethods.joins(values);
    }

    public Relation<T> leftJoins(String... values) {
        return queryMethods.leftJoins(values);
    }

    public Relation<T> where(String conditions, Object... params) {
        return queryMethods.where(conditions, params);
    }

    public Relation<T> group(String... fields) {
        return queryMethods.group(fields);
    }

    public Relation<T> having(String conditions, Object... params) {
        return queryMethods.having(conditions, params);
    }

    public Relation<T> order(String... order) {
        return queryMethods.order(order);
    }

    public Relation<T> limit(int limit) {
        return queryMethods.limit(limit);
    }

    public Relation<T> offset(int offset) {
        return queryMethods.offset(offset);
    }

    public Relation<T> distinct() {
        return queryMethods.distinct();
    }

    public Relation<T> none() {
        return queryMethods.none();
    }

    public Relation<T> includes(String... includes) {
        return queryMethods.includes(includes);
    }

    public Relation<T> eagerLoads(String... eagerLoads) {
        return queryMethods.eagerLoads(eagerLoads);
    }

    public Relation<T> reselect(String... fields) {
        return queryMethods.reselect(fields);
    }

    public Relation<T> rewhere(String conditions, Object... params) {
        return queryMethods.rewhere(conditions, params);
    }

    public Relation<T> reorder(String... fields) {
        return queryMethods.reorder(fields);
    }

    public Relation<T> lock() {
        return queryMethods.lock();
    }

    public Relation<T> from(String value) {
        return queryMethods.from(value);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="calculation">
    public long count() {
        return calculation.count();
    }

    public long count(String field) {
        return calculation.count(field);
    }

    public <R> R minimum(String field, Class<R> resultClass) {
        return (R) calculation.minimum(field, entityClass);
    }

    public <R> R maximum(String field, Class<R> resultClass) {
        return (R) calculation.maximum(field, entityClass);
    }

    public <R> R average(String field, Class<R> resultClass) {
        return (R) calculation.average(field, entityClass);
    }

    public <R> R sum(String field, Class<R> resultClass) {
        return (R) calculation.sum(field, entityClass);
    }

    public List pluck(String... fields) {
        return calculation.pluck(fields);
    }

    public List ids() {
        return calculation.ids();
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="relation methods">
    public void addSelect(String[] select) {
       range(0, select.length).forEach(i -> this.selectValues.add(select[i]));
       this.constructor = true;
    }

    public void setSelect(String select) {
       clearSelect(); this.distinct = false; this.constructor = false; this.selectValues.add(select);
    }

    public void addJoins(String[] joins) {
        range(0, joins.length).forEach(i -> this.joinsValues.add(joins[i]));
    }

    public void addLeftJoins(String[] joins) {
        range(0, joins.length).forEach(i -> this.leftJoinsValues.add(joins[i]));
    }

    public void addWhere(String where) {
        this.whereValues.add(where);
    }

    public void addParams(Object[] params) {
        range(0, params.length).forEach(i -> this.params.add(params[i]));
    }

    public void addGroup(String[] group) {
        range(0, group.length).forEach(i -> this.groupValues.add(group[i]));
    }

    public void addHaving(String having) {
        this.havingValues.add(having);
    }

    public List<String> getOrderValues() {
        return orderValues;
    }

    public void addOrder(String[] order) {
        range(0, order.length).forEach(i -> this.orderValues.add(order[i]));
    }

    public void addIncludes(String[] includes) {
        includesValues.addAll(List.of(includes));
    }

    public void addEagerLoads(String[] eagerLoads) {
        eagerLoadsValues.addAll(List.of(eagerLoads));
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public void setDistinct(boolean distinct) {
        this.distinct = distinct;
    }

    public boolean isDistinct() {
        return distinct;
    }

    public void clearSelect() {
        this.selectValues.clear();
    }

    public void clearWhere() {
        this.whereValues.clear(); this.params.clear();
    }

    public void clearOrder() {
        this.orderValues.clear();
    }

    public void setLock(boolean lock) {
        this.lock = lock;
    }

    public void setFromClause(String fromClause) {
        this.fromClause = fromClause;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="private methods">
    private String buildSelect() {
        String select = format(SELECT, distinctExp() + constructor(separatedByComma(selectOrThis())));
        String from   = format(FROM, fromClauseOrThis());
        return join(" ", select, from, "this");
    }

    private String buildJoins() {
        return format(JOIN, separatedByJoin(joinsValues));
    }

    private String buildLeftJoins() {
        return format(LEFT_JOIN, separatedByLeftJoin(leftJoinsValues));
    }

    private String buildWhere() {
        return format(WHERE, separatedByAnd(whereValues));
    }

    private String buildGroup() {
        return format(GROUP, separatedByComma(groupValues));
    }

    private String buildHaving() {
        return format(HAVING, separatedByAnd(havingValues));
    }

    private String buildOrder() {
        return format(ORDER, separatedByComma(orderValues));
    }

    private TypedQuery<T> buildParameterizedQuery(String qlString) {
        return parametize(buildQuery(qlString)).setMaxResults(limit).setFirstResult(offset);
    }

    private <R> TypedQuery<R> buildParameterizedQuery(String qlString, Class<R> resultClass) {
        return parametize(buildQuery(qlString, resultClass)).setMaxResults(limit).setFirstResult(offset);
    }

    private Query buildParameterizedQueryAlt(String qlString) {
        return parametize(buildQueryAlt(qlString)).setMaxResults(limit).setFirstResult(offset);
    }

    private <R> TypedQuery<R> parametize(TypedQuery<R> query) {
        applyParams(query); applyHints(query); return query;
    }

    private Query parametize(Query query) {
        applyParams(query); applyHints(query); return query;
    }

    private void applyParams(Query query) {
        range(0, params.size()).forEach(i -> query.setParameter(i + 1, params.get(i)));
    }

    private void applyHints(Query query) {
        query.setHint("eclipselink.batch.type", "IN");
        includesValues.forEach(value -> query.setHint("eclipselink.batch", value));
        eagerLoadsValues.forEach(value -> query.setHint("eclipselink.left-join-fetch", value));
    }

    private String separatedByAnd(List<String> values) {
        return join(" AND ", values);
    }

    private String separatedByJoin(List<String> values) {
        return join(" JOIN ", values);
    }

    private String separatedByLeftJoin(List<String> values) {
        return join(" LEFT JOIN ", values);
    }

    private String separatedByComma(List<String> values) {
        return join(", ", values);
    }

    private String distinctExp() {
        return distinct ? "DISTINCT " : "";
    }

    private List<String> selectOrThis() {
        return selectValues.isEmpty() ? Arrays.asList("this") : selectValues;
    }

    private String constructor(String fields) {
        return constructor ? format("new %s(%s)", entityClass.getName(), fields) : fields;
    }

    private String fromClauseOrThis() {
        return ofNullable(fromClause).orElse(entityClass.getSimpleName());
    }
    //</editor-fold>

}
