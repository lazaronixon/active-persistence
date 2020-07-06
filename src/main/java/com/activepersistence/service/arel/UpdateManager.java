package com.activepersistence.service.arel;

import static com.activepersistence.service.Arel.jpql;
import com.activepersistence.service.arel.nodes.JpqlLiteral;
import com.activepersistence.service.arel.nodes.Node;
import com.activepersistence.service.arel.nodes.UpdateStatement;
import static java.util.Arrays.asList;
import java.util.List;

public class UpdateManager extends TreeManager {

    private final UpdateStatement ast;

    public UpdateManager() {
        this.ast = new UpdateStatement();
    }

    public UpdateManager entity(Entity entity) {
        ast.setRelation(entity); return this;
    }

    public UpdateManager set(String values) {
        ast.setValues(asList(jpql(values))); return this;
    }

    public UpdateManager where(String condition) {
        ast.getWheres().add(jpql(condition)); return this;
    }

    public UpdateManager order(String... orders) {
        ast.getOrders().addAll(JpqlLiteral.fromArray(orders)); return this;
    }

    public void setWheres(List<Node> conditions) {
        ast.setWheres(conditions);
    }

    public void setOrders(List<Node> orders) {
        ast.setOrders(orders);
    }

    @Override
    public UpdateStatement getAst() {
        return ast;
    }
}
