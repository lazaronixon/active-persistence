package com.activepersistence.service.cases.arel;

import com.activepersistence.service.arel.Entity;
import com.activepersistence.service.models.Post;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class EntityTest {

    private Entity relation;

    @BeforeEach
    public void setup() {
        relation = new Entity(Post.class);
    }

    @Test
    public void testProject() {
        assertEquals("SELECT this FROM Post this",
                relation.project("this").toJpql());
    }

    @Test
    public void testConstructor() {
        assertEquals("SELECT NEW com.activepersistence.service.models.Post(this.id, this.title) FROM Post this",
                relation.project("this.id", "this.title").constructor(true).toJpql());
    }

    @Test
    public void testDistinct() {
        assertEquals("SELECT DISTINCT this FROM Post this",
                relation.project("this").distinct(true).toJpql());
    }

    @Test
    public void testJoin() {
        assertEquals("SELECT this FROM Post this JOIN this.comments c",
                relation.project("this").join("JOIN this.comments c").toJpql());
    }

    @Test
    public void testWhere() {
        assertEquals("SELECT this FROM Post this WHERE this.id = 1",
                relation.project("this").where("this.id = 1").toJpql());
    }

    @Test
    public void testGroup() {
        assertEquals("SELECT this.title, SUM(this.commentsCount) FROM Post this GROUP BY this.title",
                relation.project("this.title, SUM(this.commentsCount)").group("this.title").toJpql());
    }

    @Test
    public void testHaving() {
        assertEquals("SELECT this.title, SUM(this.commentsCount) FROM Post this GROUP BY this.title HAVING SUM(this.commentsCount) > 10",
                relation.project("this.title, SUM(this.commentsCount)").group("this.title").having("SUM(this.commentsCount) > 10").toJpql());
    }

    @Test
    public void testOrder() {
        assertEquals("SELECT this FROM Post this ORDER BY this.id",
                relation.project("this").order("this.id").toJpql());
    }

    @Test
    public void testFromSubQuery() {
        assertEquals("SELECT subquery FROM (SELECT this.id, this.title FROM Post this) subquery",
                relation.project("subquery").from("(SELECT this.id, this.title FROM Post this) subquery").toJpql());
    }
}
