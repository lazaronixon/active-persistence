package com.activepersistence.service.cases.arel.visitors;

import com.activepersistence.service.arel.Entity;
import com.activepersistence.service.arel.nodes.Constructor;
import com.activepersistence.service.arel.nodes.SelectCore;
import com.activepersistence.service.arel.nodes.SelectStatement;
import com.activepersistence.service.arel.nodes.SqlLiteral;
import com.activepersistence.service.arel.visitors.Visitable;
import com.activepersistence.service.arel.visitors.Visitor;
import com.activepersistence.service.models.User;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

public class ToJpql {

    private final Visitor visitor = Entity.visitor;

    @Test
    public void testVisitSelectStatement() {
        SelectStatement statement = new SelectStatement();
        statement.getCore().setSource(new Entity(User.class));
        assertEquals("SELECT FROM User this", compile(statement));
    }

    @Test
    public void testVisitSelectCore() {
        SelectCore core = new SelectCore();
        core.setSource(new Entity(User.class));
        assertEquals("SELECT FROM User this", compile(core));
    }

    @Test
    public void testVisitDistinct() {
        SelectCore core = new SelectCore();
        core.setSource(new Entity(User.class));
        core.setDistinct(true);
        core.addProjections(List.of(new SqlLiteral("this")));
        assertEquals("SELECT DISTINCT this FROM User this", compile(core));
    }

    @Test
    public void testVisitConstructor() {
        Constructor constructor = new Constructor("foo", List.of(new SqlLiteral("this.id, this.name")));
        assertEquals(" NEW foo(this.id, this.name)", compile(constructor));
    }

    @Test
    public void testVisitEntity() {
        assertEquals("User this", compile(new Entity(User.class)));
    }

    private String compile(Visitable node) {
        return visitor.accept(node, new StringBuilder()).toString();
    }

}
