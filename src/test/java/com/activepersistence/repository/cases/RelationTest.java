package com.activepersistence.repository.cases;

import com.activepersistence.ActivePersistenceError;
import com.activepersistence.IntegrationTest;
import com.activepersistence.repository.models.PostRepository;
import static com.activepersistence.repository.relation.ValueMethods.ORDER;
import java.util.Map;
import javax.inject.Inject;
import org.jboss.arquillian.persistence.UsingDataSet;
import static org.junit.Assert.*;
import org.junit.Test;

@UsingDataSet({"posts.xml", "comments.xml"})
public class RelationTest extends IntegrationTest {

    @Inject
    private PostRepository postRepository;

    @Test
    public void testScopingBlock() {
        assertEquals("SELECT post FROM Post post WHERE (1=0)",
                postRepository.all().scoping(() -> postRepository.oneEqZero()).toJpql());
    }

    @Test
    public void testScopingBlockTwice() {
        assertEquals("SELECT post FROM Post post WHERE (1=0) AND (2=0)",
                postRepository.all().scoping(() -> postRepository.oneEqZero()).scoping(() -> postRepository.twoEqZero()).toJpql());
    }

    @Test
    public void testScopingWithExcept() {
        assertEquals("SELECT post FROM Post post",
                postRepository.order("post.id").scoping(() -> postRepository.except(ORDER)).toJpql());
    }

    @Test
    public void testScopingWithUnscope() {
        assertEquals("SELECT post FROM Post post",
                postRepository.order("post.id").scoping(() -> postRepository.unscope(ORDER)).toJpql());
    }

    @Test
    public void testDestroyAll() {
        var count = (long) postRepository.count();
        postRepository.where("post.id IN (1,2,3)").destroyAll();
        assertEquals(count - 3, postRepository.count());
    }

    @Test
    public void testDestroyBy() {
        var count = (long) postRepository.count();
        postRepository.destroyBy("post.id IN (1,2,3)");
        assertEquals(count - 3, postRepository.count());
    }

    @Test
    public void testDeleteAll() {
        var count = (long) postRepository.count();
        assertEquals(3, postRepository.where("post.id IN (1,2,3)").deleteAll());
        assertEquals(count - 3, postRepository.count());
    }

    @Test
    public void testDeleteBy() {
        var count = (long) postRepository.count();
        assertEquals(3, postRepository.deleteBy("post.id IN (1,2,3)"));
        assertEquals(count - 3, postRepository.count());
    }

    @Test
    public void testDeleteAllWithDistinct() {
        assertThrows(ActivePersistenceError.class,() -> postRepository.distinct().deleteAll());
    }

    @Test
    public void testeDeleteAllWithoutConditions() {
        postRepository.deleteAll();
        assertEquals(0L, postRepository.count());
    }

    @Test
    public void testUpdateAll() {
        assertEquals(3, postRepository.where("post.id IN (1,2,3)").updateAll("post.title = 'testing'"));
        assertEquals("testing", postRepository.find(1L).getTitle());
    }

    @Test
    public void testUpdateAllMap() {
        assertEquals(3, postRepository.where("post.id IN (1,2,3)").updateAll(Map.of("post.title", "testing")));
        assertEquals("testing", postRepository.find(1L).getTitle());
    }

    @Test
    public void testUpdateAllWithDistinct() {
        assertThrows(ActivePersistenceError.class, () -> postRepository.distinct().updateAll("post.name = 'testing'"));
    }

    @Test
    public void testSize() {
        assertEquals(2L, postRepository.where("post.id IN (1, 2)").size());
    }

    @Test
    public void testIsEmpty() {
        assertTrue(postRepository.where("post.title = 'not found'").isEmpty());
    }

}
