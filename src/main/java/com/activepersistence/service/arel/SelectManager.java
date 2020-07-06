package com.activepersistence.service.arel;

import static com.activepersistence.service.Arel.jpql;
import com.activepersistence.service.arel.nodes.Node;
import com.activepersistence.service.arel.nodes.SelectCore;
import com.activepersistence.service.arel.nodes.SelectStatement;
import com.activepersistence.service.arel.nodes.SqlLiteral;
import com.activepersistence.service.arel.nodes.TableAlias;
import static java.util.Arrays.asList;
import java.util.List;

public class SelectManager extends TreeManager {

    private final SelectStatement ast;
    private final SelectCore ctx;

    public SelectManager(Entity entity) {
        this.ast = new SelectStatement(entity);
        this.ctx = this.ast.getCore();
    }

    public SelectManager project(String... projections) {
        ctx.getProjections().addAll(SqlLiteral.asList(projections)); return this;
    }

    public SelectManager project(Node... projections) {
        ctx.getProjections().addAll(asList(projections)); return this;
    }

    public SelectManager constructor(Class klass) {
        ctx.setConstructor(klass); return this;
    }

    public SelectManager distinct(boolean value) {
        ctx.setDistinct(value); return this;
    }

    public SelectManager from(String from) {
        ctx.getSource().setLeft(jpql(from)); return this;
    }

    public SelectManager from(TableAlias from) {
        ctx.getSource().setLeft(from); return this;
    }

    public SelectManager join(String join) {
        ctx.getSource().getRight().add(jpql(join)); return this;
    }

    public SelectManager where(String condition) {
        ctx.getWheres().add(jpql(condition)); return this;
    }

    public SelectManager group(String... fields) {
        ctx.getGroups().addAll(SqlLiteral.asList(fields)); return this;
    }

    public SelectManager having(String condition) {
        ctx.getHavings().add(jpql(condition)); return this;
    }

    public SelectManager order(String... expr) {
        ast.getOrders().addAll(SqlLiteral.asList(expr)); return this;
    }

    public TableAlias as(String other) {
        return new TableAlias(ast, jpql(other));
    }

    public List<Node> getConstraints() {
        return ctx.getWheres();
    }

    public List<Node> getOrders() {
        return ast.getOrders();
    }

    public List<SqlLiteral> getJoinSources() {
        return ctx.getSource().getRight();
    }

    @Override
    public SelectStatement getAst() {
        return ast;
    }

}
