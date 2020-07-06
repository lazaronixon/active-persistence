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
        Post post  = new Post("new post", "body", 0);

        long count = (long) postsService.count();
        postsService.create(post);
        assertEquals(count + 1, postsService.count());
        assertTrue(post.isPersisted());
    }

    @Test
    public void testUpdate() {
        Post post = postsService.find(1);
        post.setTitle("changed");

        long count = (long) postsService.count();
        postsService.update(post);
        assertEquals(count, postsService.count());
        assertEquals("changed", post.getTitle());
    }

    @Test
    public void testDestroy() {
        Post post = postsService.find(1);

        long count = (long) postsService.count();
        postsService.destroy(post);
        assertEquals(count -1, postsService.count());
    }

    public void testSaveCreate() {
        Post post  = new Post("new post", "body", 0);

        long count = (long) postsService.count();
        postsService.save(post);
        assertEquals(count + 1, postsService.count());
        assertTrue(post.isPersisted());
    }

    @Test
    public void testSaveUpdate() {
        Post post = postsService.find(1);
        post.setTitle("changed");

        long count = (long) postsService.count();
        postsService.update(post);
        assertEquals(count, postsService.count());
        assertEquals("changed", post.getTitle());
    }

}
