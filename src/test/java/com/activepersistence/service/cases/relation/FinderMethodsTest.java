package com.activepersistence.service.cases.relation;

import com.activepersistence.IntegrationTest;
import com.activepersistence.service.models.PostsService;
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
    public void testTake$() {
        assertThrows(NoResultException.class,() -> postsService.where("1=0").take$());
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
    public void testLast$() {
        assertThrows(NoResultException.class,() -> postsService.where("1=0").last$());
    }

    @Test
    public void testFind() {
        assertEquals((Integer) 2, postsService.find(2).getId());
    }

    @Test
    public void testFindMultiple() {
        assertEquals(2 ,postsService.find(1, 2).size());
    }

    @Test
    public void testFindBy() {
        assertEquals((Integer) 1 ,postsService.findBy("post.title = 'hello world'").getId());
    }

    @Test
    public void testFindBy$() {
        assertThrows(NoResultException.class, () -> postsService.findBy$("post.title = 'not found'"));
    }

    @Test
    public void testFindByExp() {
        assertEquals((Integer) 1 ,postsService.findByExp("IdAndTitle", 1, "hello world").getId());
    }

    @Test
    public void testFindByExp$() {
        assertThrows(NoResultException.class, () -> postsService.findByExp$("Title", "not found"));
    }

    @Test
    public void testFindById() {
        assertEquals((Integer) 1 ,postsService.findById(1).getId());
    }

    @Test
    public void testFindById$() {
        assertThrows(NoResultException.class, () -> postsService.findById$(48484));
    }

    @Test
    public void testExists() {
        assertTrue(postsService.where("post.title = 'hello world'").exists());
    }

    @Test
    public void testExistsWithParam() {
        assertTrue(postsService.exists("post.title = 'hello world'"));
    }

}
