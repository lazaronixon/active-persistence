package com.activepersistence.service.cases;

import com.activepersistence.IntegrationTest;
import com.activepersistence.service.models.Post;
import com.activepersistence.service.models.PostsService;
import javax.inject.Inject;
import org.jboss.arquillian.persistence.UsingDataSet;
import static org.junit.Assert.*;
import org.junit.Test;

@UsingDataSet({"posts.xml", "comments.xml"})
public class PersistenceTest extends IntegrationTest {

    @Inject
    private PostsService postsService;

    @Test
    public void testCreate() {
        var post  = new Post("new post", "body", 0);
        var count = (long) postsService.count();

        postsService.create(post);
        assertEquals(count + 1, postsService.count());
        assertTrue(post.isPersisted());
    }

    @Test
    public void testUpdate() {
        var post = postsService.find(1L);
        post.setTitle("changed");

        var count = (long) postsService.count();
        postsService.update(post);
        postsService.reload(post);
        assertEquals(count, postsService.count());
        assertEquals("changed", post.getTitle());
    }

    @Test
    public void testDestroy() {
        var post  = postsService.find(1L);
        var count = (long) postsService.count();

        postsService.destroy(post);
        assertEquals(count -1, postsService.count());
        assertTrue(post.isDestroyed());
    }

    public void testSaveCreate() {
        var post  = new Post("new post", "body", 0);
        var count = (long) postsService.count();

        postsService.save(post);
        assertEquals(count + 1, postsService.count());
        assertTrue(post.isPersisted());
    }

    @Test
    public void testSaveUpdate() {
        var post = postsService.find(1L);
        post.setTitle("changed");

        var count = (long) postsService.count();
        postsService.update(post);
        postsService.reload(post);
        assertEquals(count, postsService.count());
        assertEquals("changed", post.getTitle());
    }

}
