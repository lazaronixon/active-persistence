package com.activepersistence.service.cases.relation;

import com.activepersistence.IntegrationTest;
import com.activepersistence.service.models.Post;
import com.activepersistence.service.models.PostsService;
import static java.util.Arrays.asList;
import javax.inject.Inject;
import javax.persistence.NoResultException;
import org.jboss.arquillian.persistence.UsingDataSet;
import static org.junit.Assert.*;
import org.junit.Test;

@UsingDataSet({"posts.xml", "comments.xml"})
public class FinderMethodsTest extends IntegrationTest {

    @Inject
    private PostsService postsService;

    @Test
    public void testTake() {
        assertNotNull(postsService.take());
    }

    @Test
    public void testTakeWithLimit() {
        assertEquals(2, postsService.take(2).size());
    }

    @Test
    public void testTakeOrFail() {
        assertThrows(NoResultException.class,() -> postsService.where("1=0").takeOrFail());
    }

    @Test
    public void testFirst() {
        assertEquals((Integer) 1, postsService.first().getId());
    }

    @Test
    public void testFirstWithLimit() {
        assertEquals(2, postsService.first(2).size());
    }

    @Test
    public void testLast() {
        assertEquals((Integer) 9999, postsService.last().getId());
    }

    @Test
    public void testLastWithLimit() {
        assertEquals((Integer) 9999, postsService.last(2).get(0).getId());
    }

    @Test
    public void testLastOrFail() {
        assertThrows(NoResultException.class,() -> postsService.where("1=0").lastOrFail());
    }

    @Test
    public void testFind() {
        assertEquals((Integer) 2, postsService.find(2).getId());
    }

    @Test
    public void testFindMultiple() {
        assertEquals(2 ,postsService.find(asList(1, 2)).size());
    }

    @Test
    public void testFindBy() {
        assertEquals((Integer) 1 ,postsService.findBy("post.title = 'hello world'").getId());
    }

    @Test
    public void testFindByOrFail() {
        assertThrows(NoResultException.class, () -> postsService.findByOrFail("post.title = 'not found'"));
    }

    @Test
    public void testExists() {
        assertTrue(postsService.where("post.title = 'hello world'").exists());
    }

    @Test
    public void testExistsWithParam() {
        assertTrue(postsService.exists("post.title = 'hello world'"));
    }

    @Test
    public void testFindOrCreateBy() {
        long postsCount = (long) postsService.count();
        Post createdPost = postsService.findOrCreateBy("post.title = 'awesome title'", new Post("awesome title", "body", 0));
        assertEquals(postsCount + 1, postsService.count());
        assertEquals("body", createdPost.getBody());
        assertTrue(createdPost.isPersisted());
    }

    @Test
    public void testFindOrCreateByNotCreate() {
        long postsCount   = (long) postsService.count();
        Post existentPost = postsService.findOrCreateBy("post.title = 'hello world'", new Post("hello world", "body", 0));
        assertEquals(postsCount, postsService.count());
        assertEquals("My first post", existentPost.getBody());
        assertTrue(existentPost.isPersisted());
    }

    @Test
    public void testFindOrInitializeBy() {
        Post newPost = postsService.findOrInitializeBy("post.title = 'not found'", new Post("awesome title", "body", 0));
        assertEquals("body", newPost.getBody());
        assertTrue(newPost.isNewRecord());
    }

    @Test
    public void testFindOrInitializeByNotInitialize() {
        Post newPost = postsService.findOrInitializeBy("post.title = 'hello world'", new Post("hello world", "body", 0));
        assertEquals("My first post", newPost.getBody());
        assertTrue(newPost.isPersisted());
    }

}
