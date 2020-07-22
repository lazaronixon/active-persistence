package com.activepersistence.service.relation;

import com.activepersistence.service.Relation;

public class Merger {

    private final Relation relation;

    private final Relation other;

    private final Values values;

    public Merger(Relation relation, Relation other) {
        this.relation = relation;
        this.other    = other;
        this.values   = other.getValues();
    }

    public Relation merge() {
        if (shouldReplaceFromClause()) relation.getValues().setFrom(values.getFrom());
        if (shouldReplaceLockValue())  relation.lock$(values.isLock());

        if (values.isDistinct() != false) relation.distinct$(values.isDistinct());
        if (values.getLimit()   != 0)     relation.limit$(values.getLimit());
        if (values.getOffset()  != 0)     relation.offset$(values.getOffset());

        values.getJoins().forEach(this::mergeJoins$);
        values.getLeftOuterJoins().forEach(this::mergeLeftOuterJoins$);
        values.getOrder().forEach(this::mergeOrder$);

        values.getSelect().forEach(relation::select$);
        values.getWhere().forEach(relation::where$);
        values.getGroup().forEach(relation::group$);
        values.getHaving().forEach(relation::having$);
        values.getIncludes().forEach(relation::includes$);
        values.getEagerLoad().forEach(relation::eagerLoad$);
        values.getUnscope().forEach(relation::unscope$);

        return relation;
    }

    private boolean shouldReplaceLockValue() {
        return relation.getValues().isLock() == false && values.isLock();
}

    private boolean shouldReplaceFromClause() {
        return relation.getValues().getFrom() == null && values.getFrom() != null
                && relation.getEntityClass() == other.getEntityClass();
    }

    private void mergeJoins$(JoinClause joinClause) {
        if (joinClause.getAlias() == null) {
            relation.joins$(joinClause.getPath());
        } else {
            relation.joins$(joinClause.getPath(), joinClause.getAlias());
        }
    }

    private void mergeLeftOuterJoins$(JoinClause joinClause) {
        relation.leftOuterJoins$(joinClause.getPath(), joinClause.getAlias());
    }

    private void mergeOrder$(String value) {
        if (values.isReordering()) {
            relation.reorder$(value);
        } else {
            relation.order$(value);
        }
    }

}
