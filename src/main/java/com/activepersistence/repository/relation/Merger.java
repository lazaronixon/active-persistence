package com.activepersistence.repository.relation;

import com.activepersistence.repository.Relation;
import javax.persistence.LockModeType;

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
        mergeSingleValues();
        mergeMultiValues();
        mergeClauses();

        return relation;
    }

    private void mergeSingleValues() {
        if (shouldReplaceLockValue())     relation.lock$(values.getLock());
        if (values.isDistinct() != false) relation.distinct$(values.isDistinct());
        if (values.getLimit()   != 0)     relation.limit$(values.getLimit());
        if (values.getOffset()  != 0)     relation.offset$(values.getOffset());
    }

    private void mergeMultiValues() {
        relation.getValues().getWhere().addAll(values.getWhere());
        relation.getValues().getHaving().addAll(values.getHaving());
        
        values.getSelect().forEach(relation::select$);
        values.getJoins().forEach(relation::joins$);
        values.getGroup().forEach(relation::group$);
        values.getOrder().forEach(this::mergeOrder$);

        values.getIncludes().forEach(relation::includes$);
        values.getEagerLoad().forEach(relation::eagerLoad$);

        values.getUnscope().forEach(relation::unscope$);
    }

    private void mergeClauses() {
        if (shouldReplaceFromClause()) relation.getValues().setFrom(values.getFrom());
    }

    private boolean shouldReplaceLockValue() {
        return relation.getValues().getLock() == LockModeType.NONE;
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
