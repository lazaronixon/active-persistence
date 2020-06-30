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
        manager = new SelectManager(new Entity(Post.class, "this"));
    }

    @Test
    public void testProject() {
        assertEquals(manager.toJpql(), "SELECT FROM Post this");
    }

    @Test
    public void testConstruct() {
        assertEquals(manager.constructor(Post.class.getName()).toJpql(), "SELECT NEW com.activepersistence.service.models.Post() FROM Post this");
    }

    @Test
    public void testDistinct() {
        assertEquals(manager.distinct(true).toJpql(), "SELECT DISTINCT FROM Post this");
    }

    @Test
    public void testFrom() {
        assertEquals(manager.from("Another this").toJpql(), "SELECT FROM Another this");
    }

    @Test
    public void testJoin() {
        assertEquals(manager.join("JOIN this.comments c").toJpql(), "SELECT FROM Post this JOIN this.comments c");
    }

    @Test
    public void testWhere() {
        assertEquals(manager.where("this.id = 1").toJpql(), "SELECT FROM Post this WHERE this.id = 1");
    }

    @Test
    public void testGroup() {
        assertEquals(manager.group("this.id, this.title").toJpql(), "SELECT FROM Post this GROUP BY this.id, this.title");
    }

    @Test
    public void testHaving() {
        assertEquals(manager.having("SUM(this.commentsCount) > 1").toJpql(), "SELECT FROM Post this HAVING SUM(this.commentsCount) > 1");
    }

    @Test
    public void testOrder() {
        assertEquals(manager.order("this.title").toJpql(), "SELECT FROM Post this ORDER BY this.title");
    }

}
