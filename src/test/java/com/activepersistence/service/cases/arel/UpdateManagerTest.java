package com.activepersistence.service.cases.arel;

import com.activepersistence.service.arel.Entity;
import com.activepersistence.service.arel.UpdateManager;
import com.activepersistence.service.models.Post;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;

public class UpdateManagerTest {

    private UpdateManager manager;

    @Before
    public void setup() {
        manager = new UpdateManager();
        manager.entity(new Entity(Post.class, "this"));
    }

    @Test
    public void testAll() {
        assertEquals("UPDATE Post this SET this.title = 'testing'",
                manager.set("this.title = 'testing'").toJpql());
    }

    @Test
    public void testWhere() {
        assertEquals("UPDATE Post this SET this.title = 'testing' WHERE this.title = 'old'",
                manager.set("this.title = 'testing'").where("this.title = 'old'").toJpql());
    }

    @Test
    public void testOrder() {
        assertEquals("UPDATE Post this SET this.title = 'testing' ORDER BY this.id",
                manager.set("this.title = 'testing'").order("this.id").toJpql());
    }

    @Test
    public void testWhereAnd() {
        assertEquals("UPDATE Post this SET this.title = 'testing' WHERE this.title = 'old' AND this.likes_count > 0",
                manager.set("this.title = 'testing'").where("this.title = 'old'").where("this.likes_count > 0").toJpql());
    }

}
