package com.activepersistence.service.cases.relation;

import com.activepersistence.IntegrationTest;
import com.activepersistence.service.models.CommentService;
import com.activepersistence.service.models.PostService;
import static com.activepersistence.service.relation.ValueMethods.ORDER;
import javax.inject.Inject;
import org.jboss.arquillian.persistence.UsingDataSet;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import org.junit.Test;

@UsingDataSet({"posts.xml", "comments.xml"})
public class SpawnMethodsTest extends IntegrationTest {

    @Inject
    private PostService postService;

    @Inject
    private CommentService commentService;

    @Test
    public void testSpawn() {
        var relation = postService.all();
        assertNotEquals(relation, relation.spawn());
    }

    @Test
    public void testMerge() {
        assertEquals("SELECT post FROM Post post WHERE (1=0)",
                postService.merge(postService.oneEqZero()).toJpql());
    }

    @Test
    public void testMergeTwice() {
        assertEquals("SELECT post FROM Post post WHERE (1=0) AND (2=0)",
                postService.all().merge(postService.oneEqZero()).merge(postService.twoEqZero()).toJpql());
    }

    @Test
    public void testMergeWithExcept() {
        assertEquals("SELECT post FROM Post post ORDER BY post.id",
                postService.order("post.id").merge(postService.except(ORDER)).toJpql());
    }

    @Test
    public void testMergeWithUnscope() {
        assertEquals("SELECT post FROM Post post",
                postService.order("post.id").merge(postService.unscope(ORDER)).toJpql());
    }

    @Test
    public void testOnly() {
        assertEquals("SELECT post FROM Post post ORDER BY post.id",
                postService.where("1=0").order("post.id").only(ORDER).toJpql());
    }

    @Test
    public void testExcept() {
        assertEquals("SELECT post FROM Post post WHERE (1=0)",
                postService.where("1=0").order("post.id").except(ORDER).toJpql());
    }

    @Test
    public void testMergeAnotherRelation() {
        assertEquals("SELECT comment FROM Comment comment INNER JOIN comment.post post WHERE (post.id = 1)",
                commentService.joins("INNER JOIN comment.post post").merge(postService.where("post.id = 1")).toJpql());
    }

    @Test
    public void testMergeFromAnotherRelation() {
        assertEquals("SELECT comment FROM Comment comment INNER JOIN comment.post post WHERE (post.id = 1)",
                commentService.joins("INNER JOIN comment.post post").merge(postService.from("Post post").where("post.id = 1")).toJpql());
    }

    @Test
    public void testMergeOrder() {
        assertEquals("SELECT post FROM Post post ORDER BY post.id, post.title", postService.order("post.id").merge(postService.order("post.title")).toJpql());
    }

    @Test
    public void testMergeReOrder() {
        assertEquals("SELECT post FROM Post post ORDER BY post.title", postService.order("post.id").merge(postService.reorder("post.title")).toJpql());
    }

}
