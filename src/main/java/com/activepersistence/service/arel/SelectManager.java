package com.activepersistence.service.arel;

import static com.activepersistence.service.Arel.createInnerJoin;
import static com.activepersistence.service.Arel.createOuterJoin;
import static com.activepersistence.service.Arel.createStringJoin;
import static com.activepersistence.service.Arel.jpql;
import com.activepersistence.service.arel.nodes.Join;
import com.activepersistence.service.arel.nodes.JpqlLiteral;
import com.activepersistence.service.arel.nodes.Node;
import com.activepersistence.service.arel.nodes.SelectCore;
import com.activepersistence.service.arel.nodes.SelectStatement;
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
        ctx.getProjections().addAll(JpqlLiteral.fromArray(projections)); return this;
    }

    public SelectManager project(Node... projections) {
        ctx.getProjections().addAll(asList(projections)); return this;
    }

    public SelectManager constructor(boolean value) {
        ctx.setConstructor(value); return this;
    }

    public SelectManager distinct(boolean value) {
        ctx.setDistinct(value); return this;
    }

    public SelectManager from(String from) {
        ctx.getSource().setSource(jpql(from)); return this;
    }

    public SelectManager from(TableAlias from) {
        ctx.getSource().setTableAlias(from); return this;
    }

    public SelectManager join(String join) {
        ctx.getSource().getJoins().add(createStringJoin(join)); return this;
    }

    public SelectManager join(String path, String alias) {
        ctx.getSource().getJoins().add(createInnerJoin(path, alias)); return this;
    }

    public SelectManager outerJoin(String path, String alias) {
        ctx.getSource().getJoins().add(createOuterJoin(path, alias)); return this;
    }

    public SelectManager where(String condition) {
        ctx.getWheres().add(jpql(condition)); return this;
    }

    public SelectManager group(String... fields) {
        ctx.getGroups().addAll(JpqlLiteral.fromArray(fields)); return this;
    }

    public SelectManager having(String condition) {
        ctx.getHavings().add(jpql(condition)); return this;
    }

    public SelectManager order(String... expr) {
        ast.getOrders().addAll(JpqlLiteral.fromArray(expr)); return this;
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

    public List<Join> getJoinSources() {
        return ctx.getSource().getJoins();
    }

    @Override
    public SelectStatement getAst() {
        return ast;
    }

}
