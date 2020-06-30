package com.activepersistence.service.arel;

import static com.activepersistence.service.Arel.jpql;
import com.activepersistence.service.arel.nodes.Node;
import com.activepersistence.service.arel.nodes.SelectCore;
import com.activepersistence.service.arel.nodes.SelectStatement;
import com.activepersistence.service.arel.nodes.SqlLiteral;
import static java.util.Arrays.asList;

public class SelectManager {

    private final SelectStatement ast;
    private final SelectCore ctx;

    public SelectManager(Entity source) {
        this.ast = new SelectStatement();
        this.ctx = this.ast.getCore();
        this.ctx.setSource(source);
    }

    public SelectManager project(String... projections) {
        ctx.addProjections(SqlLiteral.of(projections)); return this;
    }

    public SelectManager project(Node... projections) {
        ctx.addProjections(asList(projections)); return this;
    }

    public SelectManager constructor(String name) {
        ctx.setConstructor(name); return this;
    }

    public SelectManager distinct(boolean value) {
        ctx.setDistinct(value); return this;
    }

    public SelectManager from(String from) {
        ctx.setSource(new SqlLiteral(from)); return this;
    }

    public SelectManager join(String join) {
        ctx.addJoin(jpql(join)); return this;
    }

    public SelectManager where(String condition) {
        ctx.addWhere(jpql(condition)); return this;
    }

    public SelectManager group(String... fields) {
        ctx.addGroups(SqlLiteral.of(fields)); return this;
    }

    public SelectManager having(String condition) {
        ctx.addHaving(jpql(condition)); return this;
    }

    public SelectManager order(String... expr) {
        ast.addOrders(SqlLiteral.of(expr)); return this;
    }

    public String toJpql() {
        StringBuilder collector = new StringBuilder();
        collector = Entity.visitor.accept(ast, collector);
        return collector.toString();
    }

    public SelectStatement getAst() {
        return ast;
    }

}
