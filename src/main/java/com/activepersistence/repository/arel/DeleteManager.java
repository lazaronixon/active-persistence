package com.activepersistence.repository.arel;

import static com.activepersistence.repository.Arel.jpql;
import static com.activepersistence.repository.Arel.jpqlList;
import com.activepersistence.repository.arel.nodes.DeleteStatement;
import com.activepersistence.repository.arel.visitors.Visitable;
import java.util.List;

public class DeleteManager extends TreeManager {

    private final DeleteStatement ast;

    private final DeleteStatement ctx;

    public DeleteManager() {
        this.ast = new DeleteStatement();
        this.ctx = ast;
    }

    public DeleteManager from(Entity entity) {
        ast.setRelation(entity); return this;
    }

    public DeleteManager limit(int limit) {
        ast.setLimit(limit); return this;
    }

    public DeleteManager offset(int offset) {
        ast.setOffset(offset); return this;
    }

    public DeleteManager where(String condition) {
        ctx.getWheres().add(jpql(condition)); return this;
    }

    public DeleteManager order(String... expr) {
        ast.getOrders().addAll(jpqlList(expr)); return this;
    }

    public void setWheres(List<Visitable> exprs) {
        ast.setWheres(exprs);
    }

    public void setOrders(List<Visitable> exprs) {
        ast.setOrders(exprs);
    }

    public int getLimit() {
        return ast.getLimit();
    }

    public int getOffset() {
        return ast.getOffset();
    }

    @Override
    public DeleteStatement getAst() {
        return ast;
    }

    @Override
    public DeleteStatement getCtx() {
        return ctx;
    }

}
