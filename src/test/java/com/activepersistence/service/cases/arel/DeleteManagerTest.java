package com.activepersistence.service.cases.arel;

import com.activepersistence.service.arel.DeleteManager;
import com.activepersistence.service.arel.Entity;
import com.activepersistence.service.models.Post;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;


public class DeleteManagerTest {

    private DeleteManager manager;

    @Before
    public void setup() {
        manager = new DeleteManager();
        manager.from(new Entity(Post.class, "this"));
    }

    @Test
    public void testAll() {
        assertEquals("DELETE FROM Post this", manager.toJpql());
    }

    @Test
    public void testWhere() {
        assertEquals("DELETE FROM Post this WHERE 1=0", manager.where("1=0").toJpql());
    }

    @Test
    public void testOrder() {
        assertEquals("DELETE FROM Post this ORDER BY this.id", manager.order("this.id").toJpql());
    }

    @Test
    public void testWhereAnd() {
        assertEquals("DELETE FROM Post this WHERE 1=0 AND 1=2", manager.where("1=0").where("1=2").toJpql());
    }

}
