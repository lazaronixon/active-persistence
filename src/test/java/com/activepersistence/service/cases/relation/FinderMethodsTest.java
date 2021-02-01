package com.activepersistence.service.cases.relation;

import com.activepersistence.IntegrationTest;
import com.activepersistence.service.models.PostService;
import javax.inject.Inject;
import javax.persistence.EntityNotFoundException;
import org.jboss.arquillian.persistence.UsingDataSet;
import static org.junit.Assert.*;
import org.junit.Test;

@UsingDataSet({"posts.xml", "comments.xml"})
public class FinderMethodsTest extends IntegrationTest {

    @Inject
    private PostService postService;

    @Test
    public void testTake() {
        assertNotNull(postService.take());
    }

    @Test
    public void testTakeWithLimit() {
        assertEquals(2, postService.take(2).size());
    }

    @Test
    public void testTake$() {
        assertThrows(EntityNotFoundException.class,() -> postService.where("1=0").take$());
    }

    @Test
    public void testFirst() {
        assertEquals((Long) 1L, postService.first().getId());
    }

    @Test
    public void testFirstWithLimit() {
        assertEquals(2, postService.first(2).size());
    }

    @Test
    public void testLast() {
        assertEquals((Long) 9999L, postService.last().getId());
    }

    @Test
    public void testLastWithLimit() {
        assertEquals((Long) 9999L, postService.last(2).get(0).getId());
    }

    @Test
    public void testLast$() {
        assertThrows(EntityNotFoundException.class,() -> postService.where("1=0").last$());
    }

    @Test
    public void testFindString() {
        assertEquals((Long) 2L, postService.find("2").getId());
    }

    @Test
    public void testFind() {
        assertEquals((Long) 2L, postService.find(2L).getId());
    }

    @Test
    public void testFindMultiple() {
        assertEquals(2 ,postService.find(1L, 2L).size());
    }

    @Test
    public void testFindBy() {
        assertEquals((Long) 1L ,postService.findBy("post.title = 'hello world'").getId());
    }

    @Test
    public void testFindBy$() {
        assertThrows(EntityNotFoundException.class, () -> postService.findBy$("post.title = 'not found'"));
    }

    @Test
    public void testFindByExpression() {
        assertEquals((Long) 1L ,postService.findByExpression("IdAndTitle", 1, "hello world").getId());
    }

    @Test
    public void testFindByExpression$() {
        assertThrows(EntityNotFoundException.class, () -> postService.findByExpression$("Title", "not found"));
    }

    @Test
    public void testExists() {
        assertTrue(postService.where("post.title = 'hello world'").exists());
    }

    @Test
    public void testExistsWithParam() {
        assertTrue(postService.exists("post.title = 'hello world'"));
    }

}
