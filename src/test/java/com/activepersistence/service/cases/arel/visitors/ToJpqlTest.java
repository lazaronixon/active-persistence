package com.activepersistence.service.cases.arel.visitors;

import static com.activepersistence.service.Arel.createInnerJoin;
import static com.activepersistence.service.Arel.createOuterJoin;
import static com.activepersistence.service.Arel.createStringJoin;
import static com.activepersistence.service.Arel.jpql;
import com.activepersistence.service.arel.Entity;
import com.activepersistence.service.arel.nodes.Constructor;
import com.activepersistence.service.arel.nodes.DeleteStatement;
import com.activepersistence.service.arel.nodes.JoinSource;
import com.activepersistence.service.arel.nodes.SelectCore;
import com.activepersistence.service.arel.nodes.SelectStatement;
import com.activepersistence.service.arel.nodes.TableAlias;
import com.activepersistence.service.arel.nodes.UpdateStatement;
import com.activepersistence.service.arel.visitors.Visitable;
import com.activepersistence.service.arel.visitors.Visitor;
import com.activepersistence.service.models.Post;
import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class ToJpqlTest {

    private final Visitor visitor = Entity.visitor;

    @Test
    public void testVisitDeleteStatement() {
        Entity entity = new Entity(Post.class, "post");
        DeleteStatement statement = new DeleteStatement();
        statement.setRelation(entity);
        assertEquals("DELETE FROM Post post", compile(statement));
    }

    @Test
    public void testVisitUpdateStatement() {
        Entity entity = new Entity(Post.class, "post");
        UpdateStatement statement = new UpdateStatement();
        statement.setRelation(entity);
        statement.setValues(asList(jpql("post.name = 'test'")));
        assertEquals("UPDATE Post post SET post.name = 'test'", compile(statement));
    }

    @Test
    public void testVisitSelectStatement() {
        Entity entity = new Entity(Post.class, "post");
        SelectStatement statement = new SelectStatement(entity);
        assertEquals("SELECT FROM Post post", compile(statement));
    }

    @Test
    public void testVisitSelectCore() {
        Entity entity = new Entity(Post.class, "post");
        SelectCore core = new SelectCore(entity);
        assertEquals("SELECT FROM Post post", compile(core));
    }

    @Test
    public void testVisitDistinct() {
        Entity entity = new Entity(Post.class, "post");
        SelectCore core = new SelectCore(entity);
        core.setDistinct(true);
        core.getProjections().addAll(asList(jpql("post")));
        assertEquals("SELECT DISTINCT post FROM Post post", compile(core));
    }

    @Test
    public void testVisitConstructor() {
        Constructor constructor = new Constructor("java.lang.Object", asList(jpql("post.id, post.title")));
        assertEquals(" NEW java.lang.Object(post.id, post.title)", compile(constructor));
    }

    @Test
    public void testVisitEntity() {
        assertEquals("Post post", compile(new Entity(Post.class, "post")));
    }

    @Test
    public void testVisitTableAlias() {
        SelectStatement subquery = new SelectStatement(new Entity(Post.class, "post"));
        assertEquals("(SELECT FROM Post post) subquery", compile(new TableAlias(subquery, jpql("subquery"))));
    }

    @Test
    public void testVisitJoinSource() {
        Entity entity = new Entity(Post.class, "post");
        JoinSource joinsource = new JoinSource(entity);
        joinsource.getJoins().add(createStringJoin("JOIN post.client client"));
        assertEquals("Post post JOIN post.client client", compile(joinsource));
    }

    @Test
    public void testVisitStringJoin() {
        assertEquals("JOIN Client c", compile(createStringJoin("JOIN Client c")));
    }

    @Test
    public void testVisitInnerJoin() {
        assertEquals("INNER JOIN post.comments comment", compile(createInnerJoin("post.comments", "comment")));
    }

    @Test
    public void testVisitOuterJoin() {
        Entity entity = new Entity(Post.class, "post");
        assertEquals("LEFT OUTER JOIN post.comments comment", compile(createOuterJoin("post.comments", "comment")));
    }

    @Test
    public void testVisitCount() {
        assertEquals("COUNT(post)", compile(jpql("post").count()));
    }

    @Test
    public void testVisitCountDistinct() {
        assertEquals("COUNT(DISTINCT post)", compile(jpql("post").count(true)));
    }

    @Test
    public void testVisitCountAlias() {
        assertEquals("COUNT(post) AS count_post", compile(jpql("post").count().as("count_post")));
    }

    @Test
    public void testVisitSum() {
        assertEquals("SUM(post.likesCount)", compile(jpql("post.likesCount").sum()));
    }

    @Test
    public void testVisitMaximum() {
        assertEquals("MAX(post.id)", compile(jpql("post.id").maximum()));
    }

    @Test
    public void testVisitMinimum() {
        assertEquals("MIN(post.id)", compile(jpql("post.id").minimum()));
    }

    @Test
    public void testVisitAverage() {
        assertEquals("AVG(post.id)", compile(jpql("post.id").average()));
    }

    private String compile(Visitable node) {
        return visitor.accept(node, new StringBuilder()).toString();
    }

}
