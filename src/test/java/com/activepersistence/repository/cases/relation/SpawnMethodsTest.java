package com.activepersistence.repository.cases.relation;

import com.activepersistence.IntegrationTest;
import com.activepersistence.repository.models.CommentRepository;
import com.activepersistence.repository.models.PostRepository;
import static com.activepersistence.repository.relation.ValueMethods.ORDER;
import javax.inject.Inject;
import org.jboss.arquillian.persistence.UsingDataSet;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import org.junit.Test;

@UsingDataSet({"posts.xml", "comments.xml"})
public class SpawnMethodsTest extends IntegrationTest {

    @Inject
    private PostRepository postRepository;

    @Inject
    private CommentRepository commentRepository;

    @Test
    public void testSpawn() {
        var relation = postRepository.all();
        assertNotEquals(relation, relation.spawn());
    }

    @Test
    public void testMerge() {
        assertEquals("SELECT post FROM Post post WHERE (1=0)",
                postRepository.merge(postRepository.oneEqZero()).toJpql());
    }

    @Test
    public void testMergeTwice() {
        assertEquals("SELECT post FROM Post post WHERE (1=0) AND (2=0)",
                postRepository.all().merge(postRepository.oneEqZero()).merge(postRepository.twoEqZero()).toJpql());
    }

    @Test
    public void testMergeWithExcept() {
        assertEquals("SELECT post FROM Post post ORDER BY post.id",
                postRepository.order("post.id").merge(postRepository.except(ORDER)).toJpql());
    }

    @Test
    public void testMergeWithUnscope() {
        assertEquals("SELECT post FROM Post post",
                postRepository.order("post.id").merge(postRepository.unscope(ORDER)).toJpql());
    }

    @Test
    public void testOnly() {
        assertEquals("SELECT post FROM Post post ORDER BY post.id",
                postRepository.where("1=0").order("post.id").only(ORDER).toJpql());
    }

    @Test
    public void testExcept() {
        assertEquals("SELECT post FROM Post post WHERE (1=0)",
                postRepository.where("1=0").order("post.id").except(ORDER).toJpql());
    }

    @Test
    public void testMergeAnotherRelation() {
        assertEquals("SELECT comment FROM Comment comment INNER JOIN comment.post post WHERE (post.id = 1)",
                commentRepository.joins("INNER JOIN comment.post post").merge(postRepository.where("post.id = 1")).toJpql());
    }

    @Test
    public void testMergeFromAnotherRelation() {
        assertEquals("SELECT comment FROM Comment comment INNER JOIN comment.post post WHERE (post.id = 1)",
                commentRepository.joins("INNER JOIN comment.post post").merge(postRepository.from("Post post").where("post.id = 1")).toJpql());
    }

    @Test
    public void testMergeOrder() {
        assertEquals("SELECT post FROM Post post ORDER BY post.id, post.title", postRepository.order("post.id").merge(postRepository.order("post.title")).toJpql());
    }

    @Test
    public void testMergeReOrder() {
        assertEquals("SELECT post FROM Post post ORDER BY post.title", postRepository.order("post.id").merge(postRepository.reorder("post.title")).toJpql());
    }

}
