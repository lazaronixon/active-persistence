package com.activepersistence.service;

import com.activepersistence.service.relation.Calculation;
import com.activepersistence.service.relation.FinderMethods;
import com.activepersistence.service.relation.QueryMethods;
import static java.lang.String.format;
import static java.lang.String.join;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import static java.util.Optional.ofNullable;
import javax.persistence.EntityManager;
import javax.persistence.LockModeType;
import static javax.persistence.LockModeType.NONE;
import static javax.persistence.LockModeType.PESSIMISTIC_READ;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

public class Relation<T> implements FinderMethods<T>, QueryMethods<T>, Calculation<T> {

    private static final String BATCH = "eclipselink.batch";
    
    private static final String LEFT_JOIN_FETCH = "eclipselink.left-join-fetch";

    private final EntityManager entityManager;

    private final Class<T> entityClass;

    private final Base<T> service;

    private List<String> selectValues = new ArrayList();

    private List<String> whereValues  = new ArrayList();

    private List<String> groupValues  = new ArrayList();

    private List<String> havingValues = new ArrayList();

    private List<String> orderValues  = new ArrayList();

    private List<String> joinsValues  = new ArrayList();

    private List<String> includesValues = new ArrayList();

    private List<String> eagerLoadsValues = new ArrayList();

    private HashMap<Integer, Object> ordinalParameters = new HashMap();

    private HashMap<String, Object> namedParameters = new HashMap();

    private String fromClause = null;

    private int limit  = 0;

    private int offset = 0;

    private boolean lock = false;

    private boolean distinct = false;

    private boolean constructor = false;

    private boolean calculating = false;

    private Relation<T> currentScope = null;

    public Relation(Base service) {
        this.entityManager = service.getEntityManager();
        this.entityClass   = service.getEntityClass();
        this.service       = service;
    }

    public Relation(Relation<T> other) {
        this.currentScope        = this;
        this.entityManager       = other.entityManager;
        this.entityClass         = other.entityClass;
        this.service             = other.service;
        this.selectValues        = new ArrayList(other.selectValues);
        this.whereValues         = new ArrayList(other.whereValues);
        this.groupValues         = new ArrayList(other.groupValues);
        this.havingValues        = new ArrayList(other.havingValues);
        this.orderValues         = new ArrayList(other.orderValues);
        this.joinsValues         = new ArrayList(other.joinsValues);
        this.includesValues      = new ArrayList(other.includesValues);
        this.eagerLoadsValues    = new ArrayList(other.eagerLoadsValues);
        this.ordinalParameters   = new HashMap(other.ordinalParameters);
        this.namedParameters     = new HashMap(other.namedParameters);
        this.fromClause          = other.fromClause;
        this.limit               = other.limit;
        this.offset              = other.offset;
        this.lock                = other.lock;
        this.distinct            = other.distinct;
        this.constructor         = other.constructor;
        this.calculating         = other.calculating;
    }

    public T fetchOne() {
        return buildQuery().getResultStream().findFirst().orElse(null);
    }

    public T fetchOneOrFail() {
        return buildQuery().getSingleResult();
    }

    public List<T> fetch() {
        return buildQuery().getResultList();
    }

    public List fetch_() {
        return buildQuery_().getResultList();
    }

    public boolean fetchExists() {
        return buildQuery().getResultStream().findAny().isPresent();
    }

    public Relation<T> scoping(Relation relation) {
        return new Relation(relation);
    }

    public Relation<T> unscoped() {
        return new Relation(service);
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

    public void setCalculation(String calulation) {
        this.constructor  = false;
        this.calculating  = true;
        this.selectValues = List.of(calulation);
    }

    public void addSelect(String[] select) {
        this.constructor = true;
        this.calculating = false;
        this.selectValues.addAll(List.of(select));
    }

    public void addJoins(String[] joins) {
        this.joinsValues.addAll(List.of(joins));
    }

    public void addWhere(String where) {
        this.whereValues.add(where);
    }

    public void addHaving(String having) {
        this.havingValues.add(having);
    }

    public void addGroup(String[] group) {
        this.groupValues.addAll(List.of(group));
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

    public void addOrdinalParameter(int position, Object value) {
        ordinalParameters.put(position, value);
    }

    public void addNamedParameter(String name, Object value) {
        namedParameters.put(name, value);
    }

    public void setFromClause(String fromClause) {
        this.fromClause = fromClause;
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

    public void setConstructor(boolean constructor) {
        this.constructor = constructor;
    }

    public void setCalculating(boolean calculating) {
        this.calculating = calculating;
    }

    public void clearFrom() {
        this.fromClause = null;
    }

    public void clearSelect() {
        this.constructor = false;
        this.calculating = false;
        this.selectValues.clear();
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

    public void clearWhere() {
        this.whereValues.clear();
    }

    public void clearHaving() {
        this.havingValues.clear();
    }

    public void clearOrder() {
        this.orderValues.clear();
    }

    public void setLock(boolean lock) {
        this.lock = lock;
    }

    public Relation<T> getCurrentScope() {
        return currentScope;
    }

    public Relation<T> getDefaultScope() {
        return service.defaultScope();
    }

    @Override
    public Base<T> getService() {
        return service;
    }

    @Override
    public Relation<T> spawn() {
        return new Relation(this);
    }

    @Override
    public Relation<T> thiz() {
        return this;
    }

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

    private TypedQuery<T> buildQuery() {
        return parametize(service.buildQuery(toJpql())).setLockMode(buildLockMode()).setMaxResults(limit).setFirstResult(offset);
    }

    private Query buildQuery_() {
        return parametize(service.buildQuery_(toJpql())).setLockMode(buildLockMode()).setMaxResults(limit).setFirstResult(offset);
    }

    private <R> TypedQuery<R> parametize(TypedQuery<R> query) {
        applyParams(query); applyHints(query); return query;
    }

    private Query parametize(Query query) {
        applyParams(query); applyHints(query); return query;
    }

    private void applyParams(Query query) {
        ordinalParameters.entrySet().forEach(p -> query.setParameter(p.getKey(), p.getValue()));
        namedParameters.entrySet().forEach(p -> query.setParameter(p.getKey(), p.getValue()));
    }

    private void applyHints(Query query) {
        includesValues.forEach(value -> query.setHint(BATCH, value));
        eagerLoadsValues.forEach(value -> query.setHint(LEFT_JOIN_FETCH, value));
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
        return distinct && !calculating ? "DISTINCT " : "";
    }

    private List<String> selectOrThis() {
        return selectValues.isEmpty() ? List.of("this") : selectValues;
    }

    private String fromClauseOrThis() {
        return ofNullable(fromClause).orElse(entityClass.getSimpleName());
    }

    private String constructor(String fields) {
        return constructor ? format("new %s(%s)", entityClass.getName(), fields) : fields;
    }

    private LockModeType buildLockMode() {
        return lock ? PESSIMISTIC_READ : NONE;
    }
    //</editor-fold>

}
