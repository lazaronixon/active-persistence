package com.activepersistence.service.cases;

import com.activepersistence.ActivePersistenceError;
import com.activepersistence.IntegrationTest;
import com.activepersistence.service.models.PostsService;
import static com.activepersistence.service.relation.ValueMethods.ORDER;
import java.util.Map;
import javax.inject.Inject;
import org.jboss.arquillian.persistence.UsingDataSet;
import static org.junit.Assert.*;
import org.junit.Test;

@UsingDataSet({"posts.xml", "comments.xml"})
public class RelationTest extends IntegrationTest {

    @Inject
    private PostsService postsService;

    @Test
    public void testScopingBlock() {
        assertEquals("SELECT post FROM Post post WHERE 1=0",
                postsService.all().scoping(() -> postsService.oneEqZero()).toJpql());
    }

    @Test
    public void testScopingBlockTwice() {
        assertEquals("SELECT post FROM Post post WHERE 1=0 AND 2=0",
                postsService.all().scoping(() -> postsService.oneEqZero()).scoping(() -> postsService.twoEqZero()).toJpql());
    }

    @Test
    public void testScopingWithExcept() {
        assertEquals("SELECT post FROM Post post",
                postsService.order("post.id").scoping(() -> postsService.except(ORDER)).toJpql());
    }

    @Test
    public void testScopingWithUnscope() {
        assertEquals("SELECT post FROM Post post",
                postsService.order("post.id").scoping(() -> postsService.unscope(ORDER)).toJpql());
    }

    @Test
    public void testDestroyAll() {
        var count = (long) postsService.count();
        postsService.where("post.id IN (1,2,3)").destroyAll();
        assertEquals(count - 3, postsService.count());
    }

    @Test
    public void testDestroyBy() {
        var count = (long) postsService.count();
        postsService.destroyBy("post.id IN (1,2,3)");
        assertEquals(count - 3, postsService.count());
    }

    @Test
    public void testDeleteAll() {
        var count = (long) postsService.count();
        assertEquals(3, postsService.where("post.id IN (1,2,3)").deleteAll());
        assertEquals(count - 3, postsService.count());
    }

    @Test
    public void testDeleteBy() {
        var count = (long) postsService.count();
        assertEquals(3, postsService.deleteBy("post.id IN (1,2,3)"));
        assertEquals(count - 3, postsService.count());
    }

    @Test
    public void testDeleteAllWithDistinct() {
        assertThrows(ActivePersistenceError.class,() -> postsService.distinct().deleteAll());
    }

    @Test
    public void testeDeleteAllWithoutConditions() {
        postsService.deleteAll();
        assertEquals(0L, postsService.count());
    }

    @Test
    public void testUpdateAll() {
        assertEquals(3, postsService.where("post.id IN (1,2,3)").updateAll("post.title = 'testing'"));
        assertEquals("testing", postsService.find(1L).getTitle());
    }

    @Test
    public void testUpdateAllMap() {
        assertEquals(3, postsService.where("post.id IN (1,2,3)").updateAll(Map.of("post.title", "testing")));
        assertEquals("testing", postsService.find(1L).getTitle());
    }

    @Test
    public void testUpdateAllWithDistinct() {
        assertThrows(ActivePersistenceError.class, () -> postsService.distinct().updateAll("post.name = 'testing'"));
    }

}
