package com.activepersistence.service.cases.relation;

import com.activepersistence.IntegrationTest;
import com.activepersistence.service.models.PostsService;
import java.util.List;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import org.jboss.arquillian.persistence.UsingDataSet;
import static org.junit.Assert.*;
import org.junit.Test;

@UsingDataSet({"posts.xml", "comments.xml"})
public class FinderMethodsTest extends IntegrationTest {

    @PersistenceContext
    private EntityManager em;

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
        em.createQuery("DELETE FROM Post").executeUpdate();
        assertThrows(NoResultException.class, () -> postsService.takeOrFail());
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
        // todo
    }

    @Test
    public void testLastWithLimit() {
        // todo
    }

    @Test
    public void testLastOrFail() {
        em.createQuery("DELETE FROM Post").executeUpdate();
        assertThrows(NoResultException.class, () -> postsService.lastOrFail());
    }

    @Test
    public void testFind() {
        assertEquals((Integer) 2, postsService.find(2).getId());
    }

    @Test
    public void testFindMultiple() {
        assertEquals(2 ,postsService.find(List.of(1, 2)).size());
    }

    @Test
    public void testFindBy() {
        assertEquals((Integer) 1 ,postsService.findBy("this.title = 'hello world'").getId());
    }

    @Test
    public void testFindByOrFail() {
        assertThrows(NoResultException.class, () -> postsService.findByOrFail("this.title = 'not found'"));
    }

    @Test
    public void testExists() {
        assertTrue(postsService.where("this.title = 'hello world'").exists());
    }

    @Test
    public void testExistsWithParam() {
        assertTrue(postsService.exists("this.title = 'hello world'"));
    }


}
