package com.activepersistence.service.arel;

import static com.activepersistence.service.Arel.jpql;
import static com.activepersistence.service.Arel.jpqlList;
import com.activepersistence.service.arel.nodes.Assignment;
import com.activepersistence.service.arel.nodes.UpdateStatement;
import com.activepersistence.service.arel.visitors.Visitable;
import static java.util.Arrays.asList;
import java.util.List;
import java.util.Map;
import static java.util.stream.Collectors.toList;

public class UpdateManager extends TreeManager {

    private final UpdateStatement ast;

    private final UpdateStatement ctx;

    public UpdateManager() {
        this.ast = new UpdateStatement();
        this.ctx = ast;
    }

    public UpdateManager entity(Entity entity) {
        ast.setRelation(entity); return this;
    }

    public UpdateManager set(Map<String, Object> values) {
        ast.setValues(values.entrySet().stream().map(v -> new Assignment(v.getKey(), v.getValue())).collect(toList())); return this;
    }

    public UpdateManager set(String values) {
        ast.setValues(asList(jpql(values))); return this;
    }

    public UpdateManager limit(int limit) {
        ast.setLimit(limit); return this;
    }

    public UpdateManager offset(int offset) {
        ast.setOffset(offset); return this;
    }

    public UpdateManager where(String condition) {
        ctx.getWheres().add(jpql(condition)); return this;
    }

    public UpdateManager order(String... expr) {
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
    public UpdateStatement getAst() {
        return ast;
    }

    @Override
    public UpdateStatement getCtx() {
        return ctx;
    }

}
