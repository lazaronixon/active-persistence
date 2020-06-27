package com.activepersistence.service.arel;

import com.activepersistence.service.arel.nodes.EntityAlias;
import com.activepersistence.service.arel.nodes.SelectCore;
import com.activepersistence.service.arel.nodes.SelectStatement;
import com.activepersistence.service.arel.nodes.SqlLiteral;

public class SelectManager implements FactoryMethods {

    private final SelectStatement ast;
    private final SelectCore ctx;

    public SelectManager(Source source) {
        this.ast = new SelectStatement();
        this.ctx = this.ast.getLastCore();
        this.ctx.setSource(source);
    }

    public SelectManager project(String... projections) {
        ctx.addProjections(SqlLiteral.of(projections)); return this;
    }

    public SelectManager constructor(boolean value) {
        ctx.setConstructor(value); return this;
    }

    public SelectManager distinct(boolean value) {
        ctx.setDistinct(value); return this;
    }

    public SelectManager from(Source source) {
        ctx.setSource(source); return this;
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

    public EntityAlias as(String other) {
        return createEntityAlias(grouping(ast), other);
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
