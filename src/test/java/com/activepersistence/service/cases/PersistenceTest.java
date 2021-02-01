package com.activepersistence.service.cases;

import com.activepersistence.IntegrationTest;
import com.activepersistence.service.models.Post;
import com.activepersistence.service.models.PostService;
import javax.inject.Inject;
import org.jboss.arquillian.persistence.UsingDataSet;
import static org.junit.Assert.*;
import org.junit.Test;

@UsingDataSet({"posts.xml", "comments.xml"})
public class PersistenceTest extends IntegrationTest {

    @Inject
    private PostService postService;

    @Test
    public void testDestroy() {
        var post  = postService.find(1L);
        var count = (long) postService.count();

        postService.destroy(post);

        assertEquals(count -1, postService.count());
        assertTrue(post.isDestroyed());
    }

    @Test
    public void testCreate() {
        var post  = new Post("new post", "body", 0);
        var count = (long) postService.count();

        postService.save(post);

        assertEquals(count + 1, postService.count());
        assertTrue(post.isPersisted());
    }

    @Test
    public void testUpdate() {
        var post = postService.find(1L);
        post.setTitle("changed");

        var count = (long) postService.count();

        postService.save(post);
        
        postService.reload(post);

        assertEquals(count, postService.count());
        assertEquals("changed", post.getTitle());
    }

    @Test
    public void testBeforeSave() {
        var post = new Post("new post", "execBeforeSave", 0);
        postService.save(post);

        assertEquals("OK", post.getBody());
    }

    @Test
    public void testAfterSave() {
        var post = new Post("new post", "execAfterSave", 0);
        postService.save(post);

        assertEquals("OK", post.getBody());
    }

    @Test
    public void testBeforeCreate() {
        var post = new Post("new post", "execBeforeCreate", 0);
        postService.save(post);

        assertEquals("OK", post.getBody());
    }

    @Test
    public void testAfterCreate() {
        var post = new Post("new post", "execAfterCreate", 0);
        postService.save(post);

        assertEquals("OK", post.getBody());
    }

    @Test
    public void testBeforeUpdate() {
        var post = postService.find(1L);
        post.setBody("execBeforeUpdate");

        postService.save(post);

        assertEquals("OK", post.getBody());
    }

    @Test
    public void testAfterUpdate() {
        var post = postService.find(1L);
        post.setBody("execAfterUpdate");

        postService.save(post);

        assertEquals("OK", post.getBody());
    }

    @Test
    public void testBeforeDestroy() {
        var post  = postService.find(1L);
        post.setBody("execBeforeDestroy");

        postService.destroy(post);

        assertEquals("OK", post.getBody());
    }

    @Test
    public void testAfterDestroy() {
        var post  = postService.find(1L);
        post.setBody("execAfterDestroy");

        postService.destroy(post);

        assertEquals("OK", post.getBody());
    }

}
