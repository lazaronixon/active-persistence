package com.activepersistence.service.cases.arel.visitors;

import static com.activepersistence.service.Arel.jpql;
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
        statement.getCore().getSource().setLeft(new Entity(Post.class, "this"));
        assertEquals("SELECT FROM Post this", compile(statement));
    }

    @Test
    public void testVisitSelectCore() {
        SelectCore core = new SelectCore();
        core.getSource().setLeft(new Entity(Post.class, "this"));
        assertEquals("SELECT FROM Post this", compile(core));
    }

    @Test
    public void testVisitDistinct() {
        SelectCore core = new SelectCore();
        core.getSource().setLeft(new Entity(Post.class, "this"));
        core.setDistinct(true);
        core.getProjections().addAll(SqlLiteral.of("this"));
        assertEquals("SELECT DISTINCT this FROM Post this", compile(core));
    }

    @Test
    public void testVisitConstructor() {
        Constructor constructor = new Constructor("foo", SqlLiteral.of("this.id, this.title"));
        assertEquals(" NEW foo(this.id, this.title)", compile(constructor));
    }

    @Test
    public void testVisitEntity() {
        assertEquals("Post this", compile(new Entity(Post.class, "this")));
    }

    @Test
    public void testVisitCount() {
        assertEquals("COUNT(this)", compile(jpql("this").count()));
    }

    @Test
    public void testVisitCountDistinct() {
        assertEquals("COUNT(DISTINCT this)", compile(jpql("this").count(true)));
    }

    @Test
    public void testVisitCountAlias() {
        assertEquals("COUNT(this) AS count_this", compile(jpql("this").count().as("count_this")));
    }

    @Test
    public void testVisitSum() {
        assertEquals("SUM(this.likesCount)", compile(jpql("this.likesCount").sum()));
    }

    @Test
    public void testVisitMaximum() {
        assertEquals("MAX(this.id)", compile(jpql("this.id").maximum()));
    }

    @Test
    public void testVisitMinimum() {
        assertEquals("MIN(this.id)", compile(jpql("this.id").minimum()));
    }

    @Test
    public void testVisitAverage() {
        assertEquals("AVG(this.id)", compile(jpql("this.id").average()));
    }

    private String compile(Visitable node) {
        return visitor.accept(node, new StringBuilder()).toString();
    }

}
