package com.activepersistence.service.arel.visitors;

import com.activepersistence.service.arel.Entity;
import com.activepersistence.service.arel.nodes.Assignment;
import com.activepersistence.service.arel.nodes.Avg;
import com.activepersistence.service.arel.nodes.Constructor;
import com.activepersistence.service.arel.nodes.Count;
import com.activepersistence.service.arel.nodes.DeleteStatement;
import com.activepersistence.service.arel.nodes.Distinct;
import com.activepersistence.service.arel.nodes.Function;
import com.activepersistence.service.arel.nodes.InnerJoin;
import com.activepersistence.service.arel.nodes.JoinSource;
import com.activepersistence.service.arel.nodes.JpqlLiteral;
import com.activepersistence.service.arel.nodes.Max;
import com.activepersistence.service.arel.nodes.Min;
import com.activepersistence.service.arel.nodes.OuterJoin;
import com.activepersistence.service.arel.nodes.SelectCore;
import com.activepersistence.service.arel.nodes.SelectStatement;
import com.activepersistence.service.arel.nodes.StringJoin;
import com.activepersistence.service.arel.nodes.Sum;
import com.activepersistence.service.arel.nodes.UpdateStatement;
import com.activepersistence.service.connectionadapters.Literalizing;
import java.util.List;

public class ToJpql extends Visitor {

    public StringBuilder visitDeleteStatement(DeleteStatement o, StringBuilder collector) {
        collector.append("DELETE FROM ");
        collector = visitEntity(o.getRelation(), collector);
        collectNodesFor(o.getWheres(), collector, " WHERE ", " AND ");
        collectNodesFor(o.getOrders(), collector, " ORDER BY ");
        return collector;
    }

    public StringBuilder visitUpdateStatement(UpdateStatement o, StringBuilder collector) {
        collector.append("UPDATE ");
        collector = visitEntity(o.getRelation(), collector);
        collectNodesFor(o.getValues(), collector, " SET ");
        collectNodesFor(o.getWheres(), collector, " WHERE ", " AND ");
        collectNodesFor(o.getOrders(), collector, " ORDER BY ");
        return collector;
    }

    public StringBuilder visitSelectStatement(SelectStatement o, StringBuilder collector) {
        collector = visitSelectCore(o.getCore(), collector);

        if (!o.getOrders().isEmpty()) {
            collector.append(" ORDER BY ");
            collectNodesFor(o.getOrders(), collector, "");
        }

        return collector;
    }

    public StringBuilder visitSelectCore(SelectCore o, StringBuilder collector) {
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

    public StringBuilder visitConstructor(Constructor o, StringBuilder collector) {
        collector.append(" NEW ");
        collector.append(o.getName()).append("(");
        collectNodesFor(o.getProjections(), collector, "");
        collector.append(")");

        return collector;
    }

    public StringBuilder visitEntity(Entity o, StringBuilder collector) {
        return collector.append(o.getSimpleName()).append(" ").append(o.getAlias());
    }

    public StringBuilder visitJoinSource(JoinSource o, StringBuilder collector) {
        collector = visit(o.getSource(), collector);

        if (!o.getJoins().isEmpty()) {
            collector.append(" ");
            injectJoin(o.getJoins(), collector, " ");
        }

        return collector;
    }

    public StringBuilder visitInnerJoin(InnerJoin o, StringBuilder collector) {
        collector.append("INNER JOIN ");
        collector = visit(o.getPath(), collector).append(" ").append(o.getAlias());
        if (o.getConstraint() != null) {
            collector.append(" ");
            return visit(o.getConstraint(), collector);
        } else {
            return collector;
        }
    }

    public StringBuilder visitOuterJoin(OuterJoin o, StringBuilder collector) {
        collector.append("LEFT OUTER JOIN ");
        collector = visit(o.getPath(), collector).append(" ").append(o.getAlias());
        if (o.getConstraint() != null) {
            collector.append(" ");
            return visit(o.getConstraint(), collector);
        } else {
            return collector;
        }
    }

    public StringBuilder visitStringJoin(StringJoin o, StringBuilder collector) {
        return visit(o.getPath(), collector);
    }

    public StringBuilder visitDistinct(Distinct o, StringBuilder collector) {
        return collector.append("DISTINCT");
    }

    public StringBuilder visitJpqlLiteral(JpqlLiteral o, StringBuilder collector) {
        return collector.append(o.toString());
    }

    public StringBuilder visitCount(Count o, StringBuilder collector) {
        return aggregate("COUNT", o, collector);
    }

    public StringBuilder visitSum(Sum o, StringBuilder collector) {
        return aggregate("SUM", o, collector);
    }

    public StringBuilder visitMax(Max o, StringBuilder collector) {
        return aggregate("MAX", o, collector);
    }

    public StringBuilder visitMin(Min o, StringBuilder collector) {
        return aggregate("MIN", o, collector);
    }

    public StringBuilder visitAvg(Avg o, StringBuilder collector) {
        return aggregate("AVG", o, collector);
    }

    public StringBuilder visitAssignment(Assignment o, StringBuilder collector) {
        collector = visit(o.getField(), collector);
        collector.append(" = ");
        collector.append(literal(o.getValue()));
        return collector;
    }

    public String compile(Visitable node, StringBuilder collector) {
        return accept(node, collector).toString();
    }

    private StringBuilder maybeVisit(Visitable node, StringBuilder collector) {
        return node != null ? visit(node, collector.append(" ")) : collector;
    }

    private void collectNodesFor(List<? extends Visitable> nodes, StringBuilder collector, String spacer, String connector) {
        if (!nodes.isEmpty()) collector.append(spacer); injectJoin(nodes, collector, connector);
    }

    private void collectNodesFor(List<? extends Visitable> nodes, StringBuilder collector, String spacer) {
        collectNodesFor(nodes, collector, spacer, ", ");
    }

    private StringBuilder injectJoin(List<? extends Visitable> list, StringBuilder collector, String joinStr) {
        for (var i = 0; i < list.size(); i++) { if (i != 0) collector.append(joinStr); collector = visit(list.get(i), collector); } return collector;
    }

    private StringBuilder aggregate(String name, Function o, StringBuilder collector) {
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
