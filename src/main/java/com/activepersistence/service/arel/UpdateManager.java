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

    public UpdateManager() {
        this.ast = new UpdateStatement();
    }

    public UpdateManager entity(Entity entity) {
        ast.setRelation(entity); return this;
    }

    public UpdateManager set(Map<String, String> values) {
        ast.setValues(values.entrySet().stream().map(v -> new Assignment(v.getKey(), v.getValue())).collect(toList())); return this;
    }

    public UpdateManager set(String values) {
        ast.setValues(asList(jpql(values))); return this;
    }

    public UpdateManager where(String condition) {
        ast.getWheres().add(jpql(condition)); return this;
    }

    public UpdateManager order(String... orders) {
        ast.getOrders().addAll(jpqlList(orders)); return this;
    }

    public void setWheres(List<Visitable> conditions) {
        ast.setWheres(conditions);
    }

    public void setOrders(List<Visitable> orders) {
        ast.setOrders(orders);
    }

    @Override
    public UpdateStatement getAst() {
        return ast;
    }
}
