package com.activepersistence.service.relation;

import com.activepersistence.service.Relation;

public class Merger {

    private final Relation relation;

    private final Values values;

    public Merger(Relation relation, Relation other) {
        this.relation = relation;
        this.values   = other.getValues();
    }

    public Relation merge() {
        if (shouldReplaceFromClause()) relation.getValues().setFromClause(values.getFromClause());

        if (values.isDistinctValue()) relation.distinct_(values.isDistinctValue());
        if (values.isLockValue()) relation.lock_(values.isLockValue());
        if (values.getLimitValue() != 0) relation.limit_(values.getLimitValue());
        if (values.getOffsetValue() != 0) relation.offset_(values.getOffsetValue());

        values.getSelectValues().forEach(relation::select_);
        values.getWhereValues().forEach(relation::where_);
        values.getGroupValues().forEach(relation::group_);
        values.getHavingValues().forEach(relation::having_);
        values.getOrderValues().forEach(relation::order_);
        values.getJoinsValues().forEach(relation::joins_);
        values.getIncludesValues().forEach(relation::includes_);
        values.getEagerLoadsValues().forEach(relation::eagerLoads_);
        values.getOrdinalParameters().forEach(relation::bind_);
        values.getNamedParameters().forEach(relation::bind_);

        return relation;
    }

    private boolean shouldReplaceFromClause() {
        return relation.getValues().getFromClause().isEmpty() && !values.getFromClause().isEmpty();
    }

}
