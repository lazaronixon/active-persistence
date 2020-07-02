package com.activepersistence.service.arel;

import static com.activepersistence.service.Arel.jpql;
import com.activepersistence.service.arel.nodes.Node;
import com.activepersistence.service.arel.nodes.UpdateStatement;
import static java.util.Arrays.asList;
import java.util.List;

public class UpdateManager {

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

    public void setWheres(List<Node> conditions) {
        ast.setWheres(conditions);
    }

    public String toJpql() {
        StringBuilder collector = new StringBuilder();
        collector = Entity.visitor.accept(ast, collector);
        return collector.toString();
    }

}
