package com.activepersistence.service.arel;

import com.activepersistence.service.arel.nodes.SelectCore;
import com.activepersistence.service.arel.nodes.SelectStatement;
import com.activepersistence.service.arel.nodes.SqlLiteral;

public class SelectManager {

    private final SelectStatement ast;
    private final SelectCore ctx;

    public SelectManager(Entity entity) {
        this.ast = new SelectStatement();
        this.ctx = this.ast.getCore();
        this.ctx.setSource(entity);
    }

    public SelectManager project(String... projections) {
        ctx.addProjections(SqlLiteral.of(projections)); return this;
    }

    public SelectManager distinct() {
        return this.distinct(true);
    }

    public SelectManager distinct(boolean value) {
        ctx.setDistinct(value); return this;
    }

    public SelectManager from(String clause) {
        ctx.setSource(new Entity(clause, "this")); return this;
    }

    public SelectManager join(String join) {
        ctx.addJoin(new SqlLiteral(join)); return this;
    }

    public SelectManager where(String condition) {
        ctx.addWhere(new SqlLiteral(condition)); return this;
    }

    public SelectManager group(String... fields) {
        ctx.addGroups(SqlLiteral.of(fields)); return this;
    }

    public SelectManager having(String condition) {
        ctx.addHaving(new SqlLiteral(condition)); return this;
    }

    public SelectManager order(String... expr) {
        ast.addOrders(SqlLiteral.of(expr)); return this;
    }

    public String toJpql() {
        StringBuilder collector = new StringBuilder();
        collector = Entity.visitor.accept(ast, collector);
        return collector.toString();
    }

}
