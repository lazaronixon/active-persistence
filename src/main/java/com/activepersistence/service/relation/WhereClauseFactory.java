package com.activepersistence.service.relation;

import static com.activepersistence.service.Sanitization.sanitizeJpql;
import static java.util.Arrays.asList;
import java.util.Map;

public class WhereClauseFactory {

    private PredicateBuilder predicateBuilder;

    public WhereClauseFactory() {
    }

    public WhereClause build(String opts, Object[] other) {
        return new WhereClause(asList(sanitizeJpql(opts, other)));
    }

    public WhereClause build(Map<String, Object> opts) {
        return new WhereClause(predicateBuilder.buildFromHash(opts));
    }

    public Object getPredicateBuilder() {
        return predicateBuilder;
    }

}
