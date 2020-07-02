package com.activepersistence.service.arel;

import static com.activepersistence.service.Arel.jpql;
import com.activepersistence.service.arel.nodes.DeleteStatement;
import com.activepersistence.service.arel.nodes.Node;
import java.util.List;


public class DeleteManager {

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

    public void setWheres(List<Node> conditions) {
        ast.setWheres(conditions);
    }

    public String toJpql() {
        StringBuilder collector = new StringBuilder();
        collector = Entity.visitor.accept(ast, collector);
        return collector.toString();
    }

}