package com.activepersistence.service.cases.relation;

import com.activepersistence.service.models.PostsService;
import com.activepersistence.service.relation.ValidUnscopingValues;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class QueryMethodsTest {

    private PostsService postsService;

    @BeforeEach
    public void setup() { postsService = new PostsService(); }

    @Test
    public void testAll() {
        assertEquals("SELECT this FROM Post this", postsService.all().toJpql());
    }

    @Test
    public void testSelect() {
        assertEquals("SELECT NEW com.activepersistence.service.models.Post(this.id, this.title) FROM Post this",
                postsService.select("this.id", "this.title").toJpql());
    }

    @Test
    public void testDistinct() {
        assertEquals("SELECT DISTINCT NEW com.activepersistence.service.models.Post(this.id, this.title) FROM Post this",
                postsService.select("this.id", "this.title").distinct().toJpql());
    }

    @Test
    public void testJoins() {
        assertEquals("SELECT this FROM Post this JOIN this.comments c",
                postsService.joins("JOIN this.comments c").toJpql());
    }

    @Test
    public void testWhere() {
        assertEquals("SELECT this FROM Post this WHERE this.title = 'nixon'",
                postsService.where("this.title = 'nixon'").toJpql());
    }

    @Test
    public void testGroup() {
        assertEquals("SELECT NEW com.activepersistence.service.models.Post(this.id, this.title) FROM Post this GROUP BY this.id, this.title",
                postsService.select("this.id, this.title").group("this.id, this.title").toJpql());
    }

    @Test
    public void testHaving() {
        assertEquals("SELECT NEW com.activepersistence.service.models.Post(this.id) FROM Post this GROUP BY this.id HAVING COUNT(this.title) > 0",
                postsService.select("this.id").group("this.id").having("COUNT(this.title) > 0").toJpql());
    }

    @Test
    public void testOrder() {
        assertEquals("SELECT this FROM Post this ORDER BY this.id", postsService.order("this.id").toJpql());
    }

    @Test
    public void testNone() {
        assertEquals("SELECT this FROM Post this WHERE 1=0", postsService.none().toJpql());
    }

    @Test
    public void testFrom() {
        assertEquals("SELECT NEW com.activepersistence.service.models.Post(subquery.id, subquery.title) FROM (SELECT this.id, this.title FROM Post this) subquery",
                postsService.select("subquery.id, subquery.title").from("(SELECT this.id, this.title FROM Post this) subquery").toJpql());
    }

    @Test
    public void testUnscope() {
        assertEquals("SELECT this FROM Post this", postsService.order("this.id").unscope(ValidUnscopingValues.ORDER).toJpql());
    }

    @Test
    public void testReSelect() {
        assertEquals("SELECT NEW com.activepersistence.service.models.Post(this.title) FROM Post this", postsService.select("this.id").reselect("this.title").toJpql());
    }

    @Test
    public void testReWhere() {
        assertEquals("SELECT this FROM Post this WHERE 1=0", postsService.where("1=0").rewhere("1=0").toJpql());
    }

    @Test
    public void testReOrder() {
        assertEquals("SELECT this FROM Post this ORDER BY this.id", postsService.order("this.title").reorder("this.id").toJpql());
    }

}
