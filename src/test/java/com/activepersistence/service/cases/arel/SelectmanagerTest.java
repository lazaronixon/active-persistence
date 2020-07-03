package com.activepersistence.service.cases.arel;

import com.activepersistence.service.arel.Entity;
import com.activepersistence.service.arel.SelectManager;
import com.activepersistence.service.models.Post;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;

public class SelectmanagerTest {

    private SelectManager manager;

    @Before
    public void setup() {
        manager  = new SelectManager(new Entity(Post.class, "this"));
    }

    @Test
    public void testProject() {
        assertEquals("SELECT FROM Post this", manager.toJpql());
    }

    @Test
    public void testConstruct() {
        assertEquals("SELECT NEW com.activepersistence.service.models.Post() FROM Post this",
                manager.constructor(Post.class).toJpql());
    }

    @Test
    public void testDistinct() {
        assertEquals("SELECT DISTINCT FROM Post this", manager.distinct(true).toJpql());
    }

    @Test
    public void testFrom() {
        assertEquals("SELECT FROM Another this", manager.from("Another this").toJpql());
    }

    @Test
    public void testJoin() {
        assertEquals("SELECT FROM Post this JOIN this.comments c", manager.join("JOIN this.comments c").toJpql());
    }

    @Test
    public void testWhere() {
        assertEquals("SELECT FROM Post this WHERE this.id = 1", manager.where("this.id = 1").toJpql());
    }

    @Test
    public void testGroup() {
        assertEquals("SELECT FROM Post this GROUP BY this.id, this.title", manager.group("this.id, this.title").toJpql());
    }

    @Test
    public void testHaving() {
        assertEquals("SELECT FROM Post this HAVING SUM(this.commentsCount) > 1", manager.having("SUM(this.commentsCount) > 1").toJpql());
    }

    @Test
    public void testOrder() {
        assertEquals("SELECT FROM Post this ORDER BY this.title", manager.order("this.title").toJpql());
    }

}
