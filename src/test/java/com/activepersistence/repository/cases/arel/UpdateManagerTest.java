package com.activepersistence.repository.cases.arel;

import com.activepersistence.repository.arel.Entity;
import com.activepersistence.repository.arel.UpdateManager;
import com.activepersistence.repository.models.Post;
import java.util.Map;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;

public class UpdateManagerTest {

    private UpdateManager manager;

    @Before
    public void setup() {
        manager = new UpdateManager();
        manager.entity(new Entity(Post.class, "post"));
    }

    @Test
    public void testAll() {
        assertEquals("UPDATE Post post SET post.title = 'testing'",
                manager.set("post.title = 'testing'").toJpql());
    }

    @Test
    public void testAllMap() {
        assertEquals("UPDATE Post post SET post.title = 'testing'",
                manager.set(Map.of("post.title", "testing")).toJpql());
    }

    @Test
    public void testWhere() {
        assertEquals("UPDATE Post post SET post.title = 'testing' WHERE post.title = 'old'",
                manager.set("post.title = 'testing'").where("post.title = 'old'").toJpql());
    }

    @Test
    public void testOrder() {
        assertEquals("UPDATE Post post SET post.title = 'testing' ORDER BY post.id",
                manager.set("post.title = 'testing'").order("post.id").toJpql());
    }

    @Test
    public void testWhereAnd() {
        assertEquals("UPDATE Post post SET post.title = 'testing' WHERE post.title = 'old' AND post.likes_count > 0",
                manager.set("post.title = 'testing'").where("post.title = 'old'").where("post.likes_count > 0").toJpql());
    }

}
