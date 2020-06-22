package com.activepersistence.service;

import com.activepersistence.service.relation.Calculation;
import com.activepersistence.service.relation.FinderMethods;
import com.activepersistence.service.relation.QueryMethods;
import static java.lang.String.format;
import static java.lang.String.join;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import static java.util.Optional.ofNullable;
import static java.util.regex.Pattern.compile;
import static java.util.stream.Collectors.toMap;
import static java.util.stream.IntStream.range;
import javax.persistence.EntityManager;
import static javax.persistence.LockModeType.NONE;
import static javax.persistence.LockModeType.PESSIMISTIC_READ;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

public class Relation<T> implements FinderMethods<T>, QueryMethods<T>, Calculation<T>, Querying<T> {

    private final EntityManager entityManager;

    private final Class entityClass;

    private final Base service;

    private List<String> selectValues = new ArrayList();

    private List<String> whereValues  = new ArrayList();

    private List<String> groupValues  = new ArrayList();

    private List<String> havingValues = new ArrayList();

    private List<String> orderValues  = new ArrayList();

    private List<String> joinsValues  = new ArrayList();

    private List<String> includesValues = new ArrayList();

    private List<String> eagerLoadsValues = new ArrayList();

    private HashMap<Integer, Object> whereParams = new HashMap();

    private HashMap<Integer, Object> havingParams = new HashMap();

    private String fromClause = null;

    private int limit  = 0;

    private int offset = 0;

    private boolean lock = false;

    private boolean distinct = false;

    private boolean constructor = false;

    private boolean calculating = false;

    private Relation<T> currentScope = null;

    private boolean ignoreDefaultScope = false;

    public Relation(Base service) {
        this.entityManager = service.getEntityManager();
        this.entityClass   = service.getEntityClass();
        this.service       = service;
    }

    public Relation(Relation<T> other) {
        this.currentScope       = other;
        this.entityManager      = other.entityManager;
        this.entityClass        = other.entityClass;
        this.service            = other.service;
        this.selectValues       = new ArrayList(other.selectValues);
        this.whereValues        = new ArrayList(other.whereValues);
        this.groupValues        = new ArrayList(other.groupValues);
        this.havingValues       = new ArrayList(other.havingValues);
        this.orderValues        = new ArrayList(other.orderValues);
        this.joinsValues        = new ArrayList(other.joinsValues);
        this.includesValues     = new ArrayList(other.includesValues);
        this.eagerLoadsValues   = new ArrayList(other.eagerLoadsValues);
        this.whereParams        = new HashMap(other.whereParams);
        this.havingParams       = new HashMap(other.havingParams);
        this.fromClause         = other.fromClause;
        this.limit              = other.limit;
        this.offset             = other.offset;
        this.lock               = other.lock;
        this.distinct           = other.distinct;
        this.constructor        = other.constructor;
        this.calculating        = other.calculating;
        this.ignoreDefaultScope = other.ignoreDefaultScope;
    }

    public T fetchOne() {
        return buildParameterizedQuery(toJpql()).getResultStream().findFirst().orElse(null);
    }

    public T fetchOneOrFail() {
        return buildParameterizedQuery(toJpql()).getSingleResult();
    }

    public List<T> fetch() {
        return buildParameterizedQuery(toJpql()).getResultList();
    }

    public List fetch_() {
        return buildParameterizedQuery_(toJpql()).getResultList();
    }

    public boolean fetchExists() {
        return buildParameterizedQuery(toJpql()).getResultStream().findAny().isPresent();
    }

    public Relation<T> scoping(Relation relation) {
        return new Relation(relation);
    }

    public Relation<T> unscoped() {
        return scoping(new Relation(service));
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

    public void addWhere(String where, Object[] params) {
        this.whereValues.add(where);
        this.whereParams.putAll(parseParams(where, params));
    }

    public void addGroup(String[] group) {
        this.groupValues.addAll(List.of(group));
    }

    public void addHaving(String having, Object[] params) {
        this.havingValues.add(having);
        this.havingParams.putAll(parseParams(having, params));
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

    public void clearHaving() {
        this.havingValues.clear();
        this.havingParams.clear();
    }

    public void clearWhere() {
        this.whereValues.clear();
        this.whereParams.clear();
    }

    public void clearOrder() {
        this.orderValues.clear();
    }

    public void setLock(boolean lock) {
        this.lock = lock;
    }

    public boolean hasOrderValues() {
        return !orderValues.isEmpty();
    }

    public Base getService() {
        return service;
    }

    public void setIgnoreDefaultScope(boolean ignoreDefaultScope) {
        this.ignoreDefaultScope = ignoreDefaultScope;
    }

    @Override
    public boolean isIgnoreDefaultScope() {
        return ignoreDefaultScope;
    }

    @Override
    public Relation<T> getCurrentScope() {
        return currentScope;
    }

    @Override
    public Relation<T> getDefaultScope() {
        return service.defaultScope();
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
    public String getEntityAlias() {
        return uncapitalize(entityClass.getSimpleName());
    }

    @Override
    public Relation<T> spawn() {
        return currentScope != null ? this.all() : new Relation(this);
    }

    //<editor-fold defaultstate="collapsed" desc="private methods">
    private String buildSelect() {
        String select = format("SELECT %s", distinctExp() + constructor(selectExp()));
        String from   = format("FROM %s", fromClauseOrThis());
        return join(" ", select, from, getEntityAlias());
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

    private Query buildParameterizedQuery_(String qlString) {
        return parametize(buildQuery_(qlString)).setMaxResults(limit).setFirstResult(offset);
    }

    private <R> TypedQuery<R> parametize(TypedQuery<R> query) {
        applyParams(query); applyHints(query); return query;
    }

    private Query parametize(Query query) {
        applyParams(query); applyHints(query); return query;
    }

    private void applyParams(Query query) {
        whereParams.entrySet().forEach(p -> query.setParameter(p.getKey(), p.getValue()));
        havingParams.entrySet().forEach(p -> query.setParameter(p.getKey(), p.getValue()));
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
        return distinct && !calculating ? "DISTINCT " : "";
    }

    private List<String> selectOrThis() {
        return selectValues.isEmpty() ? List.of(getEntityAlias()) : selectValues;
    }

    private String fromClauseOrThis() {
        return ofNullable(fromClause).orElse(entityClass.getSimpleName());
    }

    private String constructor(String fields) {
        return constructor ? format("new %s(%s)", entityClass.getName(), fields) : fields;
    }

    private Map<Integer, Object> parseParams(String coditions, Object[] params) {
        Integer[] indexes = indexParamsFor(coditions); return range(0, indexes.length).boxed().collect(toMap(i -> indexes[i], i -> params[i]));
    }

    private Integer[] indexParamsFor(String coditions) {
        return compile("\\?(\\d+)").matcher(coditions).results().map(r -> r.group(1)).map(Integer::parseInt).toArray(Integer[]::new);
    }

    private String uncapitalize(String value) {
        return value.substring(0, 1).toLowerCase()+ value.substring(1);
    }
    //</editor-fold>

}
