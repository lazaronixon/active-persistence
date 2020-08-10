package com.activepersistence.service.relation;

import com.activepersistence.service.Arel;
import com.activepersistence.service.arel.nodes.And;
import com.activepersistence.service.arel.nodes.Grouping;
import com.activepersistence.service.arel.nodes.JpqlLiteral;
import com.activepersistence.service.arel.visitors.Visitable;
import java.util.ArrayList;
import java.util.List;
import static java.util.stream.Collectors.toList;

public class WhereClause {

    private final List predicates;

    public WhereClause(List predicates) {
        this.predicates = predicates;
    }

    public WhereClause(WhereClause other) {
        this.predicates = new ArrayList(other.predicates);
    }

    public And getAst() {
        return new And(predicatesWithWrappedJpqlLiterals());
    }

    public boolean isEmpty() {
        return predicates.isEmpty();
    }

    public void add(WhereClause other) {
        predicates.addAll(other.predicates);
    }

    public void remove(WhereClause other) {
        predicates.removeAll(other.predicates);
    }

    public void merge(WhereClause other) {
    }

    private List<? extends Visitable> predicatesWithWrappedJpqlLiterals() {
        return (List<? extends Visitable>) predicates.stream().map(node -> {
            if (node instanceof String) {
                return wrapJpqlLiteral((String) node);
            } else if (node instanceof JpqlLiteral) {
                return wrapJpqlLiteral((JpqlLiteral) node);
            } else {
                return (Visitable) node;
            }
        }).collect(toList());
    }

    private Visitable wrapJpqlLiteral(Object node) {
        if (node instanceof String) {
            return new Grouping(Arel.jpql((String) node));
        } else {
            return new Grouping((Visitable) node);
        }
    }

    public static WhereClause empty() {
        return new WhereClause(new ArrayList());
    }

}
