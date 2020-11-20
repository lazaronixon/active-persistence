package com.activepersistence.service.cases.relation;

import com.activepersistence.IntegrationTest;
import com.activepersistence.service.models.CommentsService;
import com.activepersistence.service.models.PostsService;
import static com.activepersistence.service.relation.ValueMethods.ORDER;
import javax.inject.Inject;
import org.jboss.arquillian.persistence.UsingDataSet;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import org.junit.Test;

@UsingDataSet({"posts.xml", "comments.xml"})
public class SpawnMethodsTest extends IntegrationTest {

    @Inject
    private PostsService postsService;

    @Inject
    private CommentsService commentsService;

    @Test
    public void testSpawn() {
        var relation = postsService.all();
        assertNotEquals(relation, relation.spawn());
    }

    @Test
    public void testMerge() {
        assertEquals("SELECT post FROM Post post WHERE (1=0)",
                postsService.merge(postsService.oneEqZero()).toJpql());
    }

    @Test
    public void testMergeTwice() {
        assertEquals("SELECT post FROM Post post WHERE (1=0) AND (2=0)",
                postsService.all().merge(postsService.oneEqZero()).merge(postsService.twoEqZero()).toJpql());
    }

    @Test
    public void testMergeWithExcept() {
        assertEquals("SELECT post FROM Post post ORDER BY post.id",
                postsService.order("post.id").merge(postsService.except(ORDER)).toJpql());
    }

    @Test
    public void testMergeWithUnscope() {
        assertEquals("SELECT post FROM Post post",
                postsService.order("post.id").merge(postsService.unscope(ORDER)).toJpql());
    }

    @Test
    public void testOnly() {
        assertEquals("SELECT post FROM Post post ORDER BY post.id",
                postsService.where("1=0").order("post.id").only(ORDER).toJpql());
    }

    @Test
    public void testExcept() {
        assertEquals("SELECT post FROM Post post WHERE (1=0)",
                postsService.where("1=0").order("post.id").except(ORDER).toJpql());
    }

    @Test
    public void testMergeAnotherRelation() {
        assertEquals("SELECT comment FROM Comment comment INNER JOIN comment.post post WHERE (post.id = 1)",
                commentsService.joins("INNER JOIN comment.post post").merge(postsService.where("post.id = 1")).toJpql());
    }

    @Test
    public void testMergeFromAnotherRelation() {
        assertEquals("SELECT comment FROM Comment comment INNER JOIN comment.post post WHERE (post.id = 1)",
                commentsService.joins("INNER JOIN comment.post post").merge(postsService.from("Post post").where("post.id = 1")).toJpql());
    }

    @Test
    public void testMergeOrder() {
        assertEquals("SELECT post FROM Post post ORDER BY post.id, post.title", postsService.order("post.id").merge(postsService.order("post.title")).toJpql());
    }

    @Test
    public void testMergeReOrder() {
        assertEquals("SELECT post FROM Post post ORDER BY post.title", postsService.order("post.id").merge(postsService.reorder("post.title")).toJpql());
    }

}
