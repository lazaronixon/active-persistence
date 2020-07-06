package com.activepersistence.service.cases.arel.visitors;

import static com.activepersistence.service.Arel.jpql;
import com.activepersistence.service.arel.Entity;
import com.activepersistence.service.arel.nodes.Constructor;
import com.activepersistence.service.arel.nodes.DeleteStatement;
import com.activepersistence.service.arel.nodes.JoinSource;
import com.activepersistence.service.arel.nodes.SelectCore;
import com.activepersistence.service.arel.nodes.SelectStatement;
import com.activepersistence.service.arel.nodes.SqlLiteral;
import com.activepersistence.service.arel.nodes.TableAlias;
import com.activepersistence.service.arel.nodes.UpdateStatement;
import com.activepersistence.service.arel.visitors.Visitable;
import com.activepersistence.service.arel.visitors.Visitor;
import com.activepersistence.service.models.Post;
import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class ToJpqlTest {

    private final Visitor visitor = Entity.visitor;

    @Test
    public void testVisitDeleteStatement() {
        Entity entity = new Entity(Post.class, "this");
        DeleteStatement statement = new DeleteStatement();
        statement.setRelation(entity);
        assertEquals("DELETE FROM Post this", compile(statement));
    }

    @Test
    public void testVisitUpdateStatement() {
        Entity entity = new Entity(Post.class, "this");
        UpdateStatement statement = new UpdateStatement();
        statement.setRelation(entity);
        statement.setValues(SqlLiteral.asList("this.name = 'test'"));
        assertEquals("UPDATE Post this SET this.name = 'test'", compile(statement));
    }

    @Test
    public void testVisitSelectStatement() {
        Entity entity = new Entity(Post.class, "this");
        SelectStatement statement = new SelectStatement(entity);
        assertEquals("SELECT FROM Post this", compile(statement));
    }

    @Test
    public void testVisitSelectCore() {
        Entity entity = new Entity(Post.class, "this");
        SelectCore core = new SelectCore(entity);
        assertEquals("SELECT FROM Post this", compile(core));
    }

    @Test
    public void testVisitDistinct() {
        Entity entity = new Entity(Post.class, "this");
        SelectCore core = new SelectCore(entity);
        core.setDistinct(true);
        core.getProjections().addAll(SqlLiteral.asList("this"));
        assertEquals("SELECT DISTINCT this FROM Post this", compile(core));
    }

    @Test
    public void testVisitConstructor() {
        Constructor constructor = new Constructor(Object.class, SqlLiteral.asList("this.id, this.title"));
        assertEquals(" NEW java.lang.Object(this.id, this.title)", compile(constructor));
    }

    @Test
    public void testVisitEntity() {
        assertEquals("Post this", compile(new Entity(Post.class, "this")));
    }

    @Test
    public void testVisitTableAlias() {
        SelectStatement statement = new SelectStatement(new Entity(Post.class, "this"));
        assertEquals("(SELECT FROM Post this) subquery", compile(new TableAlias(statement, jpql("subquery"))));
    }

    @Test
    public void testVisitJoinSource() {
        JoinSource joinsource = new JoinSource(new Entity(Post.class, "this"));
        joinsource.getRight().add(jpql("JOIN Client c"));
        assertEquals("Post this JOIN Client c", compile(joinsource));
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