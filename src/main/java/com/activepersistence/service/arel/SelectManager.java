package com.activepersistence.service.arel;

import static com.activepersistence.service.Arel.jpql;
import com.activepersistence.service.arel.nodes.Node;
import com.activepersistence.service.arel.nodes.SelectCore;
import com.activepersistence.service.arel.nodes.SelectStatement;
import com.activepersistence.service.arel.nodes.SqlLiteral;
import static java.util.Arrays.asList;
import java.util.List;

public class SelectManager {

    private final SelectStatement ast;
    private final SelectCore ctx;

    public SelectManager(Entity entity) {
        this.ast = new SelectStatement();
        this.ctx = this.ast.getCore();
        this.ctx.getSource().setLeft(entity);
    }

    public SelectManager project(String... projections) {
        ctx.getProjections().addAll(SqlLiteral.of(projections)); return this;
    }

    public SelectManager project(Node... projections) {
        ctx.getProjections().addAll(asList(projections)); return this;
    }

    public SelectManager constructor(String name) {
        ctx.setConstructor(name); return this;
    }

    public SelectManager distinct(boolean value) {
        ctx.setDistinct(value); return this;
    }

    public SelectManager from(String from) {
        ctx.getSource().setLeft(jpql(from)); return this;
    }

    public SelectManager join(String join) {
        ctx.getSource().getRight().add(jpql(join)); return this;
    }

    public SelectManager where(String condition) {
        ctx.getWheres().add(jpql(condition)); return this;
    }

    public SelectManager group(String... fields) {
        ctx.getGroups().addAll(SqlLiteral.of(fields)); return this;
    }

    public SelectManager having(String condition) {
        ctx.getHavings().add(jpql(condition)); return this;
    }

    public SelectManager order(String... expr) {
        ast.getOrders().addAll(SqlLiteral.of(expr)); return this;
    }

    public List<Node> getConstraints() {
        return ctx.getWheres();
    }

    public List<SqlLiteral> getJoinSources() {
        return ctx.getSource().getRight();
    }

    public SelectStatement getAst() {
        return ast;
    }

    public String toJpql() {
        StringBuilder collector = new StringBuilder();
        collector = Entity.visitor.accept(ast, collector);
        return collector.toString();
    }

}
