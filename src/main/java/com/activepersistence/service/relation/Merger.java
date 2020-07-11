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
        if (shouldReplaceFromClause()) relation.from$(values.getFrom());
        if (shouldReplaceLockValue())  relation.lock$(values.isLock());

        if (values.isDistinct() != false) relation.distinct$(values.isDistinct());
        if (values.getLimit()   != 0)     relation.limit$(values.getLimit());
        if (values.getOffset()  != 0)     relation.offset$(values.getOffset());

        values.getSelect().forEach(relation::select$);
        values.getWhere().forEach(relation::where$);
        values.getGroup().forEach(relation::group$);
        values.getHaving().forEach(relation::having$);
        values.getOrder().forEach(relation::order$);
        values.getJoins().forEach(relation::joins$);
        values.getIncludes().forEach(relation::includes$);
        values.getEagerLoads().forEach(relation::eagerLoads$);
        values.getBind().forEach(relation::bind$);
        values.getUnscope().forEach(relation::unscope$);

        return relation;
    }

    private boolean shouldReplaceLockValue() {
        return relation.getValues().isLock() == false && values.isLock();
}

    private boolean shouldReplaceFromClause() {
        return relation.getValues().getFrom() == null && values.getFrom() != null;
    }

}
