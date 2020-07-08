package com.activepersistence.service.relation;

import com.activepersistence.service.Relation;
import com.activepersistence.service.relation.QueryMethods.ValidUnscopingValues;

public class Merger {

    private final Relation relation;

    private final Values values;

    public Merger(Relation relation, Relation other) {
        this.relation = relation;
        this.values   = other.getValues();
    }

    public Relation merge() {
        relation.unscope_(values.getUnscope().toArray(ValidUnscopingValues[]::new));

        if (shouldReplaceFromClause()) relation.from_(values.getFrom());
        if (shouldReplaceLockValue()) relation.lock_(values.isLock());

        if (values.isDistinct()) relation.distinct_(values.isDistinct());
        if (values.getLimit() != 0) relation.limit_(values.getLimit());
        if (values.getOffset() != 0) relation.offset_(values.getOffset());

        values.getSelect().forEach(relation::select_);
        values.getWhere().forEach(relation::where_);
        values.getGroup().forEach(relation::group_);
        values.getHaving().forEach(relation::having_);
        values.getOrder().forEach(relation::order_);
        values.getJoins().forEach(relation::joins_);
        values.getIncludes().forEach(relation::includes_);
        values.getEagerLoads().forEach(relation::eagerLoads_);
        values.getOrdinalBind().forEach(relation::bind_);
        values.getNamedBind().forEach(relation::bind_);

        return relation;
    }

    private boolean shouldReplaceLockValue() {
        return relation.getValues().isLock() == false && values.isLock();
    }

    private boolean shouldReplaceFromClause() {
        return relation.getValues().getFrom() == null && values.getFrom() != null;
    }

}
