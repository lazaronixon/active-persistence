package com.activepersistence.service;


import com.activepersistence.service.relation.Calculation;
import com.activepersistence.service.relation.FinderMethods;
import com.activepersistence.service.relation.QueryMethods;
import static java.lang.String.format;
import static java.lang.String.join;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import static java.util.Optional.ofNullable;
import java.util.regex.Pattern;
import static java.util.stream.IntStream.range;
import javax.persistence.EntityManager;
import static javax.persistence.LockModeType.NONE;
import static javax.persistence.LockModeType.PESSIMISTIC_READ;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

public class Relation<T> implements FinderMethods<T>, QueryMethods<T>, Calculation<T>, Querying<T> {

    private final Base base;

    private final EntityManager entityManager;

    private final Class entityClass;

    private final List<String> selectValues = new ArrayList();

    private final List<String> whereValues  = new ArrayList();

    private final List<String> groupValues  = new ArrayList();

    private final List<String> havingValues = new ArrayList();

    private final List<String> orderValues  = new ArrayList();

    private final List<String> joinsValues  = new ArrayList();

    private final List<String> includesValues = new ArrayList();

    private final List<String> eagerLoadsValues = new ArrayList();

    private final HashMap<Integer, Object> params = new HashMap();

    private String fromClause = null;

    private int limit  = 0;

    private int offset = 0;

    private boolean distinct = false;

    private boolean constructor = false;

    private boolean lock = false;

    public Relation(Base base, EntityManager entityManager, Class entityClass) {
        this.entityManager = entityManager;
        this.entityClass   = entityClass;
        this.base          = base;
    }

    public T find(Object id) {
        return getEntityManager().find(getEntityClass(), id, (lock ? PESSIMISTIC_READ : NONE));
    }

    public String toJpql() {
        StringBuilder qlString = new StringBuilder(buildSelect());
        if (!joinsValues.isEmpty())  qlString.append(" ").append(buildJoins());
        if (!whereValues.isEmpty())  qlString.append(" ").append(buildWhere());
        if (!groupValues.isEmpty())  qlString.append(" ").append(buildGroup());
        if (!havingValues.isEmpty()) qlString.append(" ").append(buildHaving());
        if (!orderValues.isEmpty())  qlString.append(" ").append(buildOrder());
        return qlString.toString();
    }

    public void setSelect(String select) {
       clearSelect(); this.distinct = false; this.selectValues.add(select);
    }

    public void addSelect(String[] select) {
        this.selectValues.addAll(List.of(select)); this.constructor = true;
    }

    public void setFromClause(String fromClause) {
        this.fromClause = fromClause;
    }

    public void addJoins(String[] joins) {
        this.joinsValues.addAll(List.of(joins));
    }

    public void addWhere(String where) {
        this.whereValues.add(where);
    }

    public void addWhere(String where, Object[] params) {
        this.whereValues.add(where);
        this.addParams(where, params);
    }

    public void addGroup(String[] group) {
        this.groupValues.addAll(List.of(group));
    }

    public void addHaving(String having, Object[] params) {
        this.havingValues.add(having);
        //if (params != null) this.params.add();
    }

    public void addOrder(String[] order) {
        this.orderValues.addAll(List.of(order));
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

    public boolean hasDistinct() {
        return distinct;
    }

    public void clearSelect() {
        this.selectValues.clear(); this.constructor = false;
    }

    public void clearFrom() {
        this.fromClause = null;
    }

    public void clearJoins() {
        this.joinsValues.clear();
    }

    public void clearGroup() {
        this.groupValues.clear();
    }

    public void clearIncludes() {
        this.includesValues.clear();
    }

    public void clearEagerLoads() {
        this.eagerLoadsValues.clear();
    }

    public void clearHaving() {
        this.havingValues.clear(); this.params.clear();
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

    public List<String> getOrderValues() {
        return orderValues;
    }

    public Base getBase() {
        return base;
    }

    @Override
    public EntityManager getEntityManager() {
        return entityManager;
    }

    @Override
    public Class<T> getEntityClass() {
        return entityClass;
    }

    @Override
    public Relation<T> getRelation() {
        return this;
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

    //<editor-fold defaultstate="collapsed" desc="private methods">
    private String buildSelect() {
        String select = format("SELECT %s", distinctExp() + constructor(selectExp()));
        String from   = format("FROM %s", fromClauseOrThis());
        return join(" ", select, from, "this");
    }

    private String selectExp() {
        return separatedByComma(selectOrThis());
    }

    private String buildJoins() {
        return separatedBySpace(joinsValues);
    }

    private String buildWhere() {
        return format("WHERE %s", separatedByAnd(whereValues));
    }

    private String buildGroup() {
        return format("GROUP BY %s", separatedByComma(groupValues));
    }

    private String buildHaving() {
        return format("HAVING %s", separatedByAnd(havingValues));
    }

    private String buildOrder() {
        return format("ORDER BY %s", separatedByComma(orderValues));
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
        params.entrySet().forEach(p -> query.setParameter(p.getKey(), p.getValue()));
    }

    private void applyHints(Query query) {
        query.setHint("eclipselink.batch.type", "IN");
        includesValues.forEach(value -> query.setHint("eclipselink.batch", value));
        eagerLoadsValues.forEach(value -> query.setHint("eclipselink.left-join-fetch", value));
    }

    private String separatedByAnd(List<String> values) {
        return join(" AND ", values);
    }

    private String separatedBySpace(List<String> values) {
        return join(" ", values);
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

    private String fromClauseOrThis() {
        return ofNullable(fromClause).orElse(entityClass.getSimpleName());
    }

    private String constructor(String fields) {
        return constructor ? format("new %s(%s)", entityClass.getName(), fields) : fields;
    }

    private void addParams(String query, Object[] params) {
        range(0, parametersFor(query).length).forEach(i -> this.params.put(parametersFor(query)[i], params[i]));
    }

    private Integer[] parametersFor(String query) {
        return Pattern.compile("\\?(\\d+)").matcher(query).results().map(r -> r.group(1)).map(Integer::parseInt).toArray(Integer[]::new);
    }

    //</editor-fold>

}
