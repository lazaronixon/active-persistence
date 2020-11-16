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
    public void testDestroy() {
        var post  = postsService.find(1L);
        var count = (long) postsService.count();

        postsService.destroy(post);

        assertEquals(count -1, postsService.count());
        assertTrue(post.isDestroyed());
    }

    public void testCreate() {
        var post  = new Post("new post", "body", 0);
        var count = (long) postsService.count();

        postsService.save(post);

        assertEquals(count + 1, postsService.count());
        assertTrue(post.isPersisted());
    }

    @Test
    public void testUpdate() {
        var post = postsService.find(1L);
        post.setTitle("changed");

        var count = (long) postsService.count();

        postsService.save(post);

        assertEquals(count, postsService.count());
        assertEquals("changed", post.getTitle());
    }

    @Test
    public void testBeforeCreate() {
        var post = new Post("new post", "execBeforeCreate", 0);
        postsService.save(post);

        assertEquals("OK", post.getBody());
    }

    @Test
    public void testAfterCreate() {
        var post = new Post("new post", "execAfterCreate", 0);
        postsService.save(post);

        assertEquals("OK", post.getBody());
    }

    @Test
    public void testBeforeUpdate() {
        var post = postsService.find(1L);
        post.setBody("execBeforeUpdate");

        postsService.save(post);

        assertEquals("OK", post.getBody());
    }

    @Test
    public void testAfterUpdate() {
        var post = postsService.find(1L);
        post.setBody("execAfterUpdate");

        postsService.save(post);

        assertEquals("OK", post.getBody());
    }

    @Test
    public void testBeforeDestroy() {
        var post  = postsService.find(1L);
        post.setBody("execBeforeDestroy");

        postsService.destroy(post);

        assertEquals("OK", post.getBody());
    }

    @Test
    public void testAfterDestroy() {
        var post  = postsService.find(1L);
        post.setBody("execAfterDestroy");

        postsService.destroy(post);

        assertEquals("OK", post.getBody());
    }

}
