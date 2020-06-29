package com.activepersistence.service.cases.arel.visitors;

import com.activepersistence.service.arel.Entity;
import com.activepersistence.service.arel.nodes.Constructor;
import com.activepersistence.service.arel.nodes.SelectCore;
import com.activepersistence.service.arel.nodes.SelectStatement;
import com.activepersistence.service.arel.nodes.SqlLiteral;
import com.activepersistence.service.arel.visitors.Visitable;
import com.activepersistence.service.arel.visitors.Visitor;
import com.activepersistence.service.models.Post;
import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class ToJpql {

    private final Visitor visitor = Entity.visitor;

    @Test
    public void testVisitSelectStatement() {
        SelectStatement statement = new SelectStatement();
        statement.getCore().setSource(new Entity(Post.class));
        assertEquals("SELECT FROM Post this", compile(statement));
    }

    @Test
    public void testVisitSelectCore() {
        SelectCore core = new SelectCore();
        core.setSource(new Entity(Post.class));
        assertEquals("SELECT FROM Post this", compile(core));
    }

    @Test
    public void testVisitDistinct() {
        SelectCore core = new SelectCore();
        core.setSource(new Entity(Post.class));
        core.setDistinct(true);
        core.addProjections(SqlLiteral.of("this"));
        assertEquals("SELECT DISTINCT this FROM Post this", compile(core));
    }

    @Test
    public void testVisitConstructor() {
        Constructor constructor = new Constructor("foo", SqlLiteral.of("this.id, this.title, this.body"));
        assertEquals(" NEW foo(this.id, this.title)", compile(constructor));
    }

    @Test
    public void testVisitEntity() {
        assertEquals("Post this", compile(new Entity(Post.class)));
    }

    private String compile(Visitable node) {
        return visitor.accept(node, new StringBuilder()).toString();
    }

}
