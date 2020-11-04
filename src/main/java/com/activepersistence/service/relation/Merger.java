package com.activepersistence.service.relation;

import com.activepersistence.service.Relation;
import javax.persistence.LockModeType;

public class Merger {

    private final Relation relation;

    private final Relation other;

    private final Values values;

    private final Boolean rewhere;

    public Merger(Relation relation, Relation other) {
        this.relation = relation;
        this.other    = other;
        this.values   = other.getValues();
        this.rewhere  = false;
    }

    public Merger(Relation relation, Relation other, Boolean rewhere) {
        this.relation = relation;
        this.other    = other;
        this.values   = other.getValues();
        this.rewhere  = rewhere;
    }

    public Relation merge() {
        mergeMultiValues();
        mergeSingleValues();
        mergeClauses();

        return relation;
    }

    private void mergeMultiValues() {
        values.getSelect().forEach(relation::select$);
        values.getJoins().forEach(relation::joins$);
        values.getGroup().forEach(relation::group$);
        values.getIncludes().forEach(relation::includes$);
        values.getEagerLoad().forEach(relation::eagerLoad$);
        values.getUnscope().forEach(relation::unscope$);
        values.getOrder().forEach(this::mergeOrder$);
    }

    private void mergeSingleValues() {
        if (shouldReplaceLockValue()) relation.lock$(values.getLock());

        if (values.isDistinct() != false) relation.distinct$(values.isDistinct());
        if (values.getLimit()   != 0)     relation.limit$(values.getLimit());
        if (values.getOffset()  != 0)     relation.offset$(values.getOffset());
    }

    private void mergeClauses() {
        if (shouldReplaceFromClause()) relation.getValues().setFrom(values.getFrom());

        var whereClause = relation.getValues().getWhere().merge(values.getWhere(), rewhere);
        if (!values.getWhere().isEmpty()) relation.getValues().setWhere(whereClause);

        var havingClause = relation.getValues().getHaving().merge(values.getHaving(), false);
        if (!values.getHaving().isEmpty()) relation.getValues().setHaving(havingClause);
    }

    private boolean shouldReplaceLockValue() {
        return relation.getValues().getLock() == LockModeType.NONE;
    }

    private boolean shouldReplaceFromClause() {
        return relation.getValues().getFrom() == null && values.getFrom() != null && relation.getEntityClass() == other.getEntityClass();
    }

    private void mergeOrder$(String value) {
        if (values.isReordering()) {
            relation.reorder$(value);
        } else {
            relation.order$(value);
        }
    }

}
