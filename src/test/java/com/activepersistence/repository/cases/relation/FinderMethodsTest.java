package com.activepersistence.repository.cases.relation;

import com.activepersistence.IntegrationTest;
import com.activepersistence.repository.models.PostRepository;
import javax.inject.Inject;
import javax.persistence.EntityNotFoundException;
import org.jboss.arquillian.persistence.UsingDataSet;
import static org.junit.Assert.*;
import org.junit.Test;

@UsingDataSet({"posts.xml", "comments.xml"})
public class FinderMethodsTest extends IntegrationTest {

    @Inject
    private PostRepository postRepository;

    @Test
    public void testTake() {
        assertNotNull(postRepository.take());
    }

    @Test
    public void testTakeWithLimit() {
        assertEquals(2, postRepository.take(2).size());
    }

    @Test
    public void testTake$() {
        assertThrows(EntityNotFoundException.class,() -> postRepository.where("1=0").take$());
    }

    @Test
    public void testFirst() {
        assertEquals((Long) 1L, postRepository.first().getId());
    }

    @Test
    public void testFirstWithLimit() {
        assertEquals(2, postRepository.first(2).size());
    }

    @Test
    public void testLast() {
        assertEquals((Long) 9999L, postRepository.last().getId());
    }

    @Test
    public void testLastWithLimit() {
        assertEquals((Long) 9999L, postRepository.last(2).get(0).getId());
    }

    @Test
    public void testLast$() {
        assertThrows(EntityNotFoundException.class,() -> postRepository.where("1=0").last$());
    }

    @Test
    public void testFindString() {
        assertEquals((Long) 2L, postRepository.find("2").getId());
    }

    @Test
    public void testFind() {
        assertEquals((Long) 2L, postRepository.find(2L).getId());
    }

    @Test
    public void testFindMultiple() {
        assertEquals(2 ,postRepository.find(1L, 2L).size());
    }

    @Test
    public void testFindBy() {
        assertEquals((Long) 1L ,postRepository.findBy("post.title = 'hello world'").getId());
    }

    @Test
    public void testFindBy$() {
        assertThrows(EntityNotFoundException.class, () -> postRepository.findBy$("post.title = 'not found'"));
    }

    @Test
    public void testFindByExpression() {
        assertEquals((Long) 1L ,postRepository.findByExpression("IdAndTitle", 1, "hello world").getId());
    }

    @Test
    public void testFindByExpression$() {
        assertThrows(EntityNotFoundException.class, () -> postRepository.findByExpression$("Title", "not found"));
    }

    @Test
    public void testExists() {
        assertTrue(postRepository.where("post.title = 'hello world'").exists());
    }

    @Test
    public void testExistsWithParam() {
        assertTrue(postRepository.exists("post.title = 'hello world'"));
    }

}
