package com.activepersistence.service.arel;

import static com.activepersistence.service.Arel.jpql;
import com.activepersistence.service.arel.nodes.DeleteStatement;
import com.activepersistence.service.arel.nodes.Node;
import com.activepersistence.service.arel.nodes.JpqlLiteral;
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
        ast.getOrders().addAll(JpqlLiteral.fromArray(orders)); return this;
    }

    public void setWheres(List<Node> conditions) {
        ast.setWheres(conditions);
    }

    public void setOrders(List<Node> orders) {
        ast.setOrders(orders);
    }

    @Override
    public DeleteStatement getAst() {
        return ast;
    }

}
