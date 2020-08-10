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

        if (values.getWhere() != null) relation.getValues().getWhere().merge(values.getWhere());

        if (values.getHaving() != null) relation.getValues().getHaving().merge(values.getHaving());

        values.getSelect().forEach(relation::select$);
        values.getJoins().forEach(relation::joins$);
        values.getLeftOuterJoins().forEach(relation::leftOuterJoins$);
        values.getGroup().forEach(relation::group$);

        values.getOrder().forEach(this::mergeOrder$);

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

    private void mergeOrder$(String value) {
        if (values.isReordering()) {
            relation.reorder$(value);
        } else {
            relation.order$(value);
        }
    }

}
