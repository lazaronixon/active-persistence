package com.activepersistence.service.cases;

import com.activepersistence.ActivePersistenceError;
import com.activepersistence.IntegrationTest;
import com.activepersistence.service.models.PostService;
import static com.activepersistence.service.relation.ValueMethods.ORDER;
import java.util.Map;
import javax.inject.Inject;
import org.jboss.arquillian.persistence.UsingDataSet;
import static org.junit.Assert.*;
import org.junit.Test;

@UsingDataSet({"posts.xml", "comments.xml"})
public class RelationTest extends IntegrationTest {

    @Inject
    private PostService postService;

    @Test
    public void testScopingBlock() {
        assertEquals("SELECT post FROM Post post WHERE (1=0)",
                postService.all().scoping(() -> postService.oneEqZero()).toJpql());
    }

    @Test
    public void testScopingBlockTwice() {
        assertEquals("SELECT post FROM Post post WHERE (1=0) AND (2=0)",
                postService.all().scoping(() -> postService.oneEqZero()).scoping(() -> postService.twoEqZero()).toJpql());
    }

    @Test
    public void testScopingWithExcept() {
        assertEquals("SELECT post FROM Post post",
                postService.order("post.id").scoping(() -> postService.except(ORDER)).toJpql());
    }

    @Test
    public void testScopingWithUnscope() {
        assertEquals("SELECT post FROM Post post",
                postService.order("post.id").scoping(() -> postService.unscope(ORDER)).toJpql());
    }

    @Test
    public void testDestroyAll() {
        var count = (long) postService.count();
        postService.where("post.id IN (1,2,3)").destroyAll();
        assertEquals(count - 3, postService.count());
    }

    @Test
    public void testDestroyBy() {
        var count = (long) postService.count();
        postService.destroyBy("post.id IN (1,2,3)");
        assertEquals(count - 3, postService.count());
    }

    @Test
    public void testDeleteAll() {
        var count = (long) postService.count();
        assertEquals(3, postService.where("post.id IN (1,2,3)").deleteAll());
        assertEquals(count - 3, postService.count());
    }

    @Test
    public void testDeleteBy() {
        var count = (long) postService.count();
        assertEquals(3, postService.deleteBy("post.id IN (1,2,3)"));
        assertEquals(count - 3, postService.count());
    }

    @Test
    public void testDeleteAllWithDistinct() {
        assertThrows(ActivePersistenceError.class,() -> postService.distinct().deleteAll());
    }

    @Test
    public void testeDeleteAllWithoutConditions() {
        postService.deleteAll();
        assertEquals(0L, postService.count());
    }

    @Test
    public void testUpdateAll() {
        assertEquals(3, postService.where("post.id IN (1,2,3)").updateAll("post.title = 'testing'"));
        assertEquals("testing", postService.find(1L).getTitle());
    }

    @Test
    public void testUpdateAllMap() {
        assertEquals(3, postService.where("post.id IN (1,2,3)").updateAll(Map.of("post.title", "testing")));
        assertEquals("testing", postService.find(1L).getTitle());
    }

    @Test
    public void testUpdateAllWithDistinct() {
        assertThrows(ActivePersistenceError.class, () -> postService.distinct().updateAll("post.name = 'testing'"));
    }

    @Test
    public void testSize() {
        assertEquals(2L, postService.where("post.id IN (1, 2)").size());
    }

    @Test
    public void testIsEmpty() {
        assertTrue(postService.where("post.title = 'not found'").isEmpty());
    }

}
