package com.activepersistence.repository.cases.arel;

import com.activepersistence.repository.arel.Entity;
import com.activepersistence.repository.arel.SelectManager;
import com.activepersistence.repository.models.Post;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;

public class SelectmanagerTest {

    private SelectManager manager;

    @Before
    public void setup() {
        manager  = new SelectManager(new Entity(Post.class, "post"));
    }

    @Test
    public void testProject() {
        assertEquals("SELECT FROM Post post", manager.toJpql());
    }

    @Test
    public void testConstruct() {
        assertEquals("SELECT NEW com.activepersistence.repository.models.Post() FROM Post post", manager.constructor(true).toJpql());
    }

    @Test
    public void testDistinct() {
        assertEquals("SELECT DISTINCT FROM Post post", manager.distinct(true).toJpql());
    }

    @Test
    public void testFrom() {
        assertEquals("SELECT FROM Another post", manager.from("Another post").toJpql());
    }

    @Test
    public void testJoin() {
        assertEquals("SELECT FROM Post post JOIN post.comments c", manager.join("JOIN post.comments c").toJpql());
    }

    @Test
    public void testWhere() {
        assertEquals("SELECT FROM Post post WHERE post.id = 1", manager.where("post.id = 1").toJpql());
    }

    @Test
    public void testGroup() {
        assertEquals("SELECT FROM Post post GROUP BY post.id, post.title", manager.group("post.id, post.title").toJpql());
    }

    @Test
    public void testHaving() {
        assertEquals("SELECT FROM Post post HAVING SUM(post.commentsCount) > 1", manager.having("SUM(post.commentsCount) > 1").toJpql());
    }

    @Test
    public void testOrder() {
        assertEquals("SELECT FROM Post post ORDER BY post.title", manager.order("post.title").toJpql());
    }

}
