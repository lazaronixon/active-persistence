package com.activepersistence.service.relation;

import static com.activepersistence.service.Arel.jpql;
import com.activepersistence.service.arel.nodes.And;
import com.activepersistence.service.arel.nodes.JpqlLiteral;
import com.activepersistence.service.arel.visitors.Visitable;
import java.util.ArrayList;
import java.util.List;
import static java.util.stream.Collectors.toList;
import java.util.stream.Stream;
import static java.util.stream.Stream.concat;

public class WhereClause {

    private List predicates;

    WhereClause() {
        this.predicates = new ArrayList();
    }

    WhereClause(List predicates) {
        this.predicates = predicates;
    }

    WhereClause(WhereClause other) {
        this.predicates = new ArrayList(other.predicates);
    }

    public WhereClause addAll(WhereClause other) {
        predicates.addAll(other.predicates); return new WhereClause(predicates);
    }

    public WhereClause removeAll(WhereClause other) {
        predicates.removeAll(other.predicates); return new WhereClause(predicates);
    }

    public void clear() {
        predicates.clear();
    }

    public boolean isEmpty() {
        return predicates.isEmpty();
    }

    public WhereClause merge(WhereClause other, Boolean rewhere) {
        if (rewhere) {
            predicates = exceptPredicates(other.predicates);
        } else {
            predicates = predicatesUnreferencedBy(other);
        }

        return new WhereClause(predicatesUnion(predicates, other.predicates));
    }

    public Visitable getAst() {
        var nodes = predicatesWithWrappedSqlLiterals().collect(toList()); return nodes.size() == 1 ? nodes.get(0) : new And(nodes);
    }

    private List predicatesUnion(List predicatesA, List predicatesB) {
        return (List) concat(predicatesA.stream(), predicatesB.stream()).distinct().collect(toList());
    }

    private List<Visitable> exceptPredicates(List predicates) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private List<Visitable> predicatesUnreferencedBy(WhereClause other) {
        return predicates;
    }

    private Stream<Visitable> predicatesWithWrappedSqlLiterals() {
        return nonEmptyPredicates().map(node -> {
          if (node instanceof String || node instanceof JpqlLiteral) {
            return wrapJpqlLiteral(node);
          }  else {
            return node;
          }
        });
    }

    private Stream nonEmptyPredicates() {
        return predicates.stream().filter(node -> !node.equals(""));
    }

    private Visitable wrapJpqlLiteral(Object node) {
        if (node instanceof String) {
            return jpql((String) node);
        } else {
            return (Visitable) node;
        }
    }

}
