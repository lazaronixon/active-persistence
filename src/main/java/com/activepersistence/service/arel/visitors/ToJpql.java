package com.activepersistence.service.arel.visitors;

import com.activepersistence.service.arel.Entity;
import com.activepersistence.service.arel.nodes.Avg;
import com.activepersistence.service.arel.nodes.Constructor;
import com.activepersistence.service.arel.nodes.Count;
import com.activepersistence.service.arel.nodes.Distinct;
import com.activepersistence.service.arel.nodes.Function;
import com.activepersistence.service.arel.nodes.Max;
import com.activepersistence.service.arel.nodes.Min;
import com.activepersistence.service.arel.nodes.SelectCore;
import com.activepersistence.service.arel.nodes.SelectStatement;
import com.activepersistence.service.arel.nodes.SqlLiteral;
import com.activepersistence.service.arel.nodes.Sum;
import java.util.List;

public class ToJpql extends Visitor {

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

        if (o.getSetConstructor() != null) {
            collector = visitConstructor(o.getSetConstructor(), collector);
        } else {
            collectNodesFor(o.getProjections(), collector, " ");
        }

        collector.append(" FROM ");
        collector = visit(o.getSource(), collector);

        collectNodesFor(o.getJoins(), collector, " ", " ");
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

    public StringBuilder visitDistinct(Distinct o, StringBuilder collector) {
        return collector.append("DISTINCT");
    }

    public StringBuilder visitSqlLiteral(SqlLiteral o, StringBuilder collector) {
        return collector.append(o);
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

    private StringBuilder maybeVisit(Visitable thing, StringBuilder collector) {
        return thing != null ? visit(thing, collector.append(" ")) : collector;
    }

    private void collectNodesFor(List nodes, StringBuilder collector, String spacer, String connector) {
        if (!nodes.isEmpty()) collector.append(spacer); injectJoin(nodes, collector, connector);
    }

    private void collectNodesFor(List nodes, StringBuilder collector, String spacer) {
        collectNodesFor(nodes, collector, spacer, ", ");
    }

    private void injectJoin(List<Visitable> list, StringBuilder collector, String joinStr) {
        for(int i = 0; i < list.size(); i++) { if (i != 0) collector.append(joinStr); collector = visit(list.get(i), collector); }
    }

    private StringBuilder aggregate(String name, Function o, StringBuilder collector) {
        collector.append(name).append("(");
        if (o.isDistinct()) collector.append("DISTINCT ");
        collector.append(o.getExpression()).append(")");
        if (o.getAlias() != null) { collector.append(" AS "); visitSqlLiteral(o.getAlias(), collector); }
        return collector;
    }
}
