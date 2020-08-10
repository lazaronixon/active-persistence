package com.activepersistence.service.arel;

import static com.activepersistence.service.Arel.jpql;
import static com.activepersistence.service.Arel.jpqlList;
import com.activepersistence.service.arel.nodes.DeleteStatement;
import com.activepersistence.service.arel.visitors.Visitable;
import java.util.List;

public class DeleteManager extends TreeManager {

    private final DeleteStatement ast;

    public DeleteManager() {
        this.ast = new DeleteStatement();
    }

    public DeleteManager from(Entity entity) {
        ast.setRelation(entity); return this;
    }

    public DeleteManager where(String condition) {
        ast.getWheres().add(jpql(condition)); return this;
    }

    public DeleteManager order(String... orders) {
        ast.getOrders().addAll(jpqlList(orders)); return this;
    }

    public void setWheres(List<Visitable> conditions) {
        ast.setWheres(conditions);
    }

    public void setOrders(List<Visitable> orders) {
        ast.setOrders(orders);
    }

    @Override
    public DeleteStatement getAst() {
        return ast;
    }

}
