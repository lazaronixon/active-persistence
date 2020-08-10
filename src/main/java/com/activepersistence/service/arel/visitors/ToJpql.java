package com.activepersistence.service.arel.visitors;

import com.activepersistence.service.Literalizing;
import com.activepersistence.service.arel.Attribute;
import com.activepersistence.service.arel.Entity;
import com.activepersistence.service.arel.collectors.JPQLString;
import com.activepersistence.service.arel.nodes.And;
import com.activepersistence.service.arel.nodes.Assignment;
import com.activepersistence.service.arel.nodes.Avg;
import com.activepersistence.service.arel.nodes.BindParam;
import com.activepersistence.service.arel.nodes.Constructor;
import com.activepersistence.service.arel.nodes.Count;
import com.activepersistence.service.arel.nodes.DeleteStatement;
import com.activepersistence.service.arel.nodes.Distinct;
import com.activepersistence.service.arel.nodes.Equality;
import com.activepersistence.service.arel.nodes.Function;
import com.activepersistence.service.arel.nodes.Grouping;
import com.activepersistence.service.arel.nodes.InnerJoin;
import com.activepersistence.service.arel.nodes.JoinSource;
import com.activepersistence.service.arel.nodes.JpqlLiteral;
import com.activepersistence.service.arel.nodes.Literalized;
import com.activepersistence.service.arel.nodes.Max;
import com.activepersistence.service.arel.nodes.Min;
import com.activepersistence.service.arel.nodes.Node;
import com.activepersistence.service.arel.nodes.NotEqual;
import com.activepersistence.service.arel.nodes.OuterJoin;
import com.activepersistence.service.arel.nodes.SelectCore;
import com.activepersistence.service.arel.nodes.SelectStatement;
import com.activepersistence.service.arel.nodes.StringJoin;
import com.activepersistence.service.arel.nodes.Sum;
import com.activepersistence.service.arel.nodes.UpdateStatement;
import java.util.List;

public class ToJpql extends Visitor {

    public JPQLString visitDeleteStatement(DeleteStatement o, JPQLString collector) {
        collector.append("DELETE FROM ");
        collector = visitEntity(o.getRelation(), collector);
        collectNodesFor(o.getWheres(), collector, " WHERE ", " AND ");
        collectNodesFor(o.getOrders(), collector, " ORDER BY ");
        return collector;
    }

    public JPQLString visitUpdateStatement(UpdateStatement o, JPQLString collector) {
        collector.append("UPDATE ");
        collector = visitEntity(o.getRelation(), collector);
        collectNodesFor(o.getValues(), collector, " SET ");
        collectNodesFor(o.getWheres(), collector, " WHERE ", " AND ");
        collectNodesFor(o.getOrders(), collector, " ORDER BY ");
        return collector;
    }

    public JPQLString visitSelectStatement(SelectStatement o, JPQLString collector) {
        collector = visitSelectCore(o.getCore(), collector);

        if (!o.getOrders().isEmpty()) {
            collector.append(" ORDER BY ");
            collectNodesFor(o.getOrders(), collector, "");
        }

        return collector;
    }

    public JPQLString visitSelectCore(SelectCore o, JPQLString collector) {
        collector.append("SELECT");

        collector = maybeVisit(o.getSetQuantifier(), collector);

        if (o.getConstructor() != null) {
            collector = visitConstructor(o.getConstructor(), collector);
        } else {
            collectNodesFor(o.getProjections(), collector, " ");
        }

        collector.append(" FROM ");
        collector = visit(o.getSource(), collector);

        collectNodesFor(o.getWheres(), collector, " WHERE ", " AND ");
        collectNodesFor(o.getGroups(), collector, " GROUP BY ");
        collectNodesFor(o.getHavings(), collector, " HAVING ", " AND ");

        return collector;
    }

    public JPQLString visitConstructor(Constructor o, JPQLString collector) {
        collector.append(" NEW ");
        collector.append(o.getName()).append("(");
        collectNodesFor(o.getProjections(), collector, "");
        collector.append(")");

        return collector;
    }

    public JPQLString visitEntity(Entity o, JPQLString collector) {
        return collector.append(o.getSimpleName()).append(" ").append(o.getAlias());
    }

    public JPQLString visitJoinSource(JoinSource o, JPQLString collector) {
        collector = visit(o.getSource(), collector);

        if (!o.getJoins().isEmpty()) {
            collector.append(" ");
            injectJoin(o.getJoins(), collector, " ");
        }

        return collector;
    }

    public JPQLString visitInnerJoin(InnerJoin o, JPQLString collector) {
        collector.append("INNER JOIN ");
        collector = visit(o.getPath(), collector).append(" ").append(o.getAlias());
        if (o.getConstraint() != null) {
            collector.append(" ");
            return visit(o.getConstraint(), collector);
        } else {
            return collector;
        }
    }

    public JPQLString visitOuterJoin(OuterJoin o, JPQLString collector) {
        collector.append("LEFT OUTER JOIN ");
        collector = visit(o.getPath(), collector).append(" ").append(o.getAlias());
        if (o.getConstraint() != null) {
            collector.append(" ");
            return visit(o.getConstraint(), collector);
        } else {
            return collector;
        }
    }

    public JPQLString visitStringJoin(StringJoin o, JPQLString collector) {
        return visit(o.getPath(), collector);
    }

    public JPQLString visitDistinct(Distinct o, JPQLString collector) {
        return collector.append("DISTINCT");
    }

    public JPQLString visitJpqlLiteral(JpqlLiteral o, JPQLString collector) {
        return collector.append(o.toString());
    }

    public JPQLString visitCount(Count o, JPQLString collector) {
        return aggregate("COUNT", o, collector);
    }

    public JPQLString visitSum(Sum o, JPQLString collector) {
        return aggregate("SUM", o, collector);
    }

    public JPQLString visitMax(Max o, JPQLString collector) {
        return aggregate("MAX", o, collector);
    }

    public JPQLString visitMin(Min o, JPQLString collector) {
        return aggregate("MIN", o, collector);
    }

    public JPQLString visitAvg(Avg o, JPQLString collector) {
        return aggregate("AVG", o, collector);
    }

    public JPQLString visitAttribute(Attribute o, JPQLString collector) {
        var joinName = o.getRelation().getAlias();
        return collector.append(joinName).append(".").append(o.getName());
    }

    public JPQLString visitAssignment(Assignment o, JPQLString collector) {
        if (o.getField() instanceof Node || o.getField() instanceof Attribute) {
            collector = visit(o.getField(), collector);
            collector.append(" = ");
            visit(((Visitable) o.getValue()), collector);
        } else {
            collector = visit(o.getField(), collector);
            collector.append(" = ");
            collector.append(literal(o.getValue()));
        }
        return collector;
    }

    public JPQLString visitEquality(Equality o, JPQLString collector) {
        collector = visit(o.getLeft(), collector);
        if (o.getRight()== null) {
            return collector.append(" IS NULL");
        } else {
            collector.append(" = ");
            return visit(o.getRight(), collector);
        }
    }

    public JPQLString visitNotEqual(NotEqual o, JPQLString collector) {
        collector = visit(o.getLeft(), collector);
        if (o.getRight()== null) {
            return collector.append(" IS NOT NULL");
        } else {
            collector.append(" != ");
            return visit(o.getRight(), collector);
        }
    }

    public JPQLString visitAnd(And o, JPQLString collector) {
        return injectJoin(o.getChildren(), collector, " AND ");
    }

    public JPQLString visitGrouping(Grouping o, JPQLString collector) {
        if (o.getValue() instanceof Grouping) {
            return visit(o.getValue(), collector);
        } else {
            collector.append("(");
            return visit(o.getValue(), collector).append(")");
        }
    }

    public JPQLString visitLiteralized(Literalized o, JPQLString collector) {
        return collector.append(literal(o.getValue()));
    }

    public JPQLString visitBindParam(BindParam o, JPQLString collector) {
        return collector.addBind(o.getValue(), i -> "?" + i);
    }

    private JPQLString maybeVisit(Visitable thing, JPQLString collector) {
        return thing != null ? visit(thing, collector.append(" ")) : collector;
    }

    private void collectNodesFor(List<? extends Visitable> nodes, JPQLString collector, String spacer, String connector) {
        if (!nodes.isEmpty()) collector.append(spacer); injectJoin(nodes, collector, connector);
    }

    private void collectNodesFor(List<? extends Visitable> nodes, JPQLString collector, String spacer) {
        collectNodesFor(nodes, collector, spacer, ", ");
    }

    private JPQLString injectJoin(List<? extends Visitable> list, JPQLString collector, String joinStr) {
        for(var i = 0; i < list.size(); i++) { if (i != 0) collector.append(joinStr); collector = visit(list.get(i), collector); }; return collector;
    }

    private JPQLString aggregate(String name, Function o, JPQLString collector) {
        collector.append(name).append("(");
        if (o.isDistinct()) collector.append("DISTINCT ");
        collector.append(o.getExpression()).append(")");
        if (o.getAlias() != null) { collector.append(" AS "); visitJpqlLiteral(o.getAlias(), collector); }
        return collector;
    }

    private String literal(Object value) {
        if (value instanceof JpqlLiteral) {
            return value.toString();
        } else {
            return Literalizing.literal(value);
        }
    }
}
