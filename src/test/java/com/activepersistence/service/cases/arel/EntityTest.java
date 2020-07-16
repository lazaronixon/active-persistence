package com.activepersistence.service.cases.arel;

import static com.activepersistence.service.Arel.jpql;
import com.activepersistence.service.arel.Entity;
import com.activepersistence.service.models.Post;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;

public class EntityTest {

    private Entity relation;

    @Before
    public void setup() {
        relation = new Entity(Post.class, "post");
    }

    @Test
    public void testProject() {
        assertEquals("SELECT post FROM Post post",
                relation.project("post").toJpql());
    }

    @Test
    public void testProjectNodes() {
        assertEquals("SELECT post FROM Post post",
                relation.project(jpql("post")).toJpql());
    }

    @Test
    public void testConstructor() {
        assertEquals("SELECT NEW com.activepersistence.service.models.Post(post.id, post.title) FROM Post post",
                relation.project("post.id", "post.title").constructor(true).toJpql());
    }

    @Test
    public void testDistinct() {
        assertEquals("SELECT DISTINCT post FROM Post post",
                relation.project("post").distinct(true).toJpql());
    }

    @Test
    public void testJoin() {
        assertEquals("SELECT post FROM Post post JOIN post.comments c",
                relation.project("post").join("JOIN post.comments c").toJpql());
    }

    @Test
    public void testWhere() {
        assertEquals("SELECT post FROM Post post WHERE post.id = 1",
                relation.project("post").where("post.id = 1").toJpql());
    }

    @Test
    public void testGroup() {
        assertEquals("SELECT post.title, SUM(post.commentsCount) FROM Post post GROUP BY post.title",
                relation.project("post.title, SUM(post.commentsCount)").group("post.title").toJpql());
    }

    @Test
    public void testHaving() {
        assertEquals("SELECT post.title, SUM(post.commentsCount) FROM Post post GROUP BY post.title HAVING SUM(post.commentsCount) > 10",
                relation.project("post.title, SUM(post.commentsCount)").group("post.title").having("SUM(post.commentsCount) > 10").toJpql());
    }

    @Test
    public void testOrder() {
        assertEquals("SELECT post FROM Post post ORDER BY post.id",
                relation.project("post").order("post.id").toJpql());
    }

    @Test
    public void testFromSubQuery() {
        assertEquals("SELECT subquery FROM (SELECT post.id, post.title FROM Post post) subquery",
                relation.project("subquery").from("(SELECT post.id, post.title FROM Post post) subquery").toJpql());
    }
}
