package com.activepersistence.repository.cases.arel;

import com.activepersistence.repository.arel.DeleteManager;
import com.activepersistence.repository.arel.Entity;
import com.activepersistence.repository.models.Post;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;


public class DeleteManagerTest {

    private DeleteManager manager;

    @Before
    public void setup() {
        manager = new DeleteManager();
        manager.from(new Entity(Post.class, "post"));
    }

    @Test
    public void testAll() {
        assertEquals("DELETE FROM Post post", manager.toJpql());
    }

    @Test
    public void testWhere() {
        assertEquals("DELETE FROM Post post WHERE 1=0", manager.where("1=0").toJpql());
    }

    @Test
    public void testOrder() {
        assertEquals("DELETE FROM Post post ORDER BY post.id", manager.order("post.id").toJpql());
    }

    @Test
    public void testWhereAnd() {
        assertEquals("DELETE FROM Post post WHERE 1=0 AND 1=2", manager.where("1=0").where("1=2").toJpql());
    }

}
