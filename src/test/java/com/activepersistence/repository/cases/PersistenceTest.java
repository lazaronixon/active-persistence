package com.activepersistence.repository.cases;

import com.activepersistence.IntegrationTest;
import com.activepersistence.repository.models.Post;
import com.activepersistence.repository.models.PostRepository;
import javax.inject.Inject;
import org.jboss.arquillian.persistence.UsingDataSet;
import static org.junit.Assert.*;
import org.junit.Test;

@UsingDataSet({"posts.xml", "comments.xml"})
public class PersistenceTest extends IntegrationTest {

    @Inject
    private PostRepository postRepository;

    @Test
    public void testDestroy() {
        var post  = postRepository.find(1L);
        var count = (long) postRepository.count();

        postRepository.destroy(post);

        assertEquals(count -1, postRepository.count());
        assertTrue(post.isDestroyed());
    }

    @Test
    public void testCreate() {
        var post  = new Post("new post", "body", 0);
        var count = (long) postRepository.count();

        postRepository.save(post);

        assertEquals(count + 1, postRepository.count());
        assertTrue(post.isPersisted());
    }

    @Test
    public void testUpdate() {
        var post = postRepository.find(1L);
        post.setTitle("changed");

        var count = (long) postRepository.count();

        postRepository.save(post);
        
        postRepository.reload(post);

        assertEquals(count, postRepository.count());
        assertEquals("changed", post.getTitle());
    }

    @Test
    public void testBeforeSave() {
        var post = new Post("new post", "execBeforeSave", 0);
        postRepository.save(post);

        assertEquals("OK", post.getBody());
    }

    @Test
    public void testAfterSave() {
        var post = new Post("new post", "execAfterSave", 0);
        postRepository.save(post);

        assertEquals("OK", post.getBody());
    }

    @Test
    public void testBeforeCreate() {
        var post = new Post("new post", "execBeforeCreate", 0);
        postRepository.save(post);

        assertEquals("OK", post.getBody());
    }

    @Test
    public void testAfterCreate() {
        var post = new Post("new post", "execAfterCreate", 0);
        postRepository.save(post);

        assertEquals("OK", post.getBody());
    }

    @Test
    public void testBeforeUpdate() {
        var post = postRepository.find(1L);
        post.setBody("execBeforeUpdate");

        postRepository.save(post);

        assertEquals("OK", post.getBody());
    }

    @Test
    public void testAfterUpdate() {
        var post = postRepository.find(1L);
        post.setBody("execAfterUpdate");

        postRepository.save(post);

        assertEquals("OK", post.getBody());
    }

    @Test
    public void testBeforeDestroy() {
        var post  = postRepository.find(1L);
        post.setBody("execBeforeDestroy");

        postRepository.destroy(post);

        assertEquals("OK", post.getBody());
    }

    @Test
    public void testAfterDestroy() {
        var post  = postRepository.find(1L);
        post.setBody("execAfterDestroy");

        postRepository.destroy(post);

        assertEquals("OK", post.getBody());
    }

}
