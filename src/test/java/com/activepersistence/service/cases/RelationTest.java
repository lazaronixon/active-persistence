package com.activepersistence.service.cases;

import com.activepersistence.ActivePersistenceError;
import com.activepersistence.IntegrationTest;
import com.activepersistence.service.models.PostsService;
import static com.activepersistence.service.relation.ValueMethods.ORDER;
import javax.inject.Inject;
import javax.persistence.NoResultException;
import org.jboss.arquillian.persistence.UsingDataSet;
import static org.junit.Assert.*;
import org.junit.Test;

@UsingDataSet({"posts.xml", "comments.xml"})
public class RelationTest extends IntegrationTest {

    @Inject
    private PostsService postsService;

    @Test
    public void testScopingBlock() {
        assertEquals("SELECT this FROM Post this WHERE 1=0",
                postsService.all().scoping(() -> postsService.oneEqZero()).toJpql());
    }

    @Test
    public void testScopingBlockTwice() {
        assertEquals("SELECT this FROM Post this WHERE 1=0 AND 2=0",
                postsService.all().scoping(() -> postsService.oneEqZero()).scoping(() -> postsService.twoEqZero()).toJpql());
    }

    @Test
    public void testScopingWithExcept() {
        assertEquals("SELECT this FROM Post this",
                postsService.order("this.id").scoping(() -> postsService.except(ORDER)).toJpql());
    }

    @Test
    public void testScopingWithUnscope() {
        assertEquals("SELECT this FROM Post this",
                postsService.order("this.id").scoping(() -> postsService.unscope(ORDER)).toJpql());
    }

    @Test
    public void testFetchOne() {
        assertNotNull(postsService.where("this.id = 1").fetchOne());
    }

    @Test
    public void testFetchOneOrFail() {
        assertThrows(NoResultException.class,() -> postsService.where("1=0").fetchOneOrFail());
    }

    @Test
    public void testFetchExists() {
        assertTrue(postsService.where("this.id = 1").fetchExists());
    }

    @Test
    public void testFetch() {
        assertNotNull(postsService.all().fetch());
    }

    @Test
    public void testFetch$() {
        assertNotNull(postsService.all().fetch$());
    }

    @Test
    public void testDestroyAll() {
        long count = (long) postsService.count();
        postsService.where("this.id IN (1,2,3)").destroyAll();
        assertEquals(count - 3, postsService.count());
    }

    @Test
    public void testDestroyBy() {
        long count = (long) postsService.count();
        postsService.destroyBy("this.id IN (1,2,3)");
        assertEquals(count - 3, postsService.count());
    }

    @Test
    public void testDeleteAll() {
        long count = (long) postsService.count();
        assertEquals(3, postsService.where("this.id IN (1,2,3)").deleteAll());
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
    public void testDeleteBy() {
        long count = (long) postsService.count();
        assertEquals(3, postsService.deleteBy("this.id IN (1,2,3)"));
        assertEquals(count - 3, postsService.count());
    }

    @Test
    public void testUpdateAll() {
        assertEquals(3, postsService.where("this.id IN (1,2,3)").updateAll("this.title = 'testing'"));
        assertEquals("testing", postsService.find(1).getTitle());
    }

    @Test
    public void testUpdateAllWithDistinct() {
        assertThrows(ActivePersistenceError.class, () -> postsService.distinct().updateAll("this.name = 'testing'"));
    }

}
