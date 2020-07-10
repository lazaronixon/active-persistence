package com.activepersistence.service.cases.relation;

import com.activepersistence.IntegrationTest;
import com.activepersistence.service.Relation;
import com.activepersistence.service.models.Post;
import com.activepersistence.service.models.PostsService;
import javax.inject.Inject;
import org.jboss.arquillian.persistence.UsingDataSet;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import org.junit.Test;

@UsingDataSet({"posts.xml", "comments.xml"})
public class SpawnMethodsTest extends IntegrationTest {

    @Inject
    private PostsService postsService;

    @Test
    public void testSpawn() {
        Relation<Post> relation = postsService.all();
        assertNotEquals(relation, relation.spawn());
    }

    @Test
    public void testMerge() {
        assertEquals("SELECT this FROM Post this WHERE 1=0",
                postsService.merge(postsService.oneEqZero()).toJpql());
    }

    @Test
    public void testMergeTwice() {
        assertEquals("SELECT this FROM Post this WHERE 1=0 AND 2=0",
                postsService.all().merge(postsService.oneEqZero()).merge(postsService.twoEqZero()).toJpql());
    }

    @Test
    public void testMergeWithExcept() {
        assertEquals("SELECT this FROM Post this ORDER BY this.id",
                postsService.order("this.id").merge(postsService.except("order")).toJpql());
    }

    @Test
    public void testMergeWithUnscope() {
        assertEquals("SELECT this FROM Post this", postsService.order("this.id").merge(postsService.unscope("order")).toJpql());
    }

    @Test
    public void testOnly() {
        assertEquals("SELECT this FROM Post this ORDER BY this.id",
                postsService.where("1=0").order("this.id").only("order").toJpql());
    }

    @Test
    public void testExcept() {
        assertEquals("SELECT this FROM Post this WHERE 1=0",
                postsService.where("1=0").order("this.id").except("order").toJpql());
    }

}
