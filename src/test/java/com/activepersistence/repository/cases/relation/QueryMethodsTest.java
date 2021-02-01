package com.activepersistence.repository.cases.relation;

import com.activepersistence.IntegrationTest;
import com.activepersistence.ReadOnlyRecord;
import com.activepersistence.repository.models.PostRepository;
import static com.activepersistence.repository.relation.ValueMethods.FROM;
import static com.activepersistence.repository.relation.ValueMethods.ORDER;
import java.util.Map;
import javax.inject.Inject;
import org.jboss.arquillian.persistence.UsingDataSet;
import static org.junit.Assert.*;
import org.junit.Test;

@UsingDataSet({"posts.xml", "comments.xml", "clients.xml"})
public class QueryMethodsTest extends IntegrationTest {

    @Inject
    private PostRepository postRepository;

    @Test
    public void testSelect() {
        assertEquals("SELECT NEW com.activepersistence.repository.models.Post(post.id, post.title) FROM Post post",
                postRepository.select("post.id", "post.title").toJpql());
    }

    @Test
    public void testSelectScalar() {
        assertEquals("SELECT post.id, post.title FROM Post post",
                postRepository.select2("post.id", "post.title").toJpql());
    }

    @Test
    public void testDistinct() {
        assertEquals("SELECT DISTINCT NEW com.activepersistence.repository.models.Post(post.id, post.title) FROM Post post",
                postRepository.select("post.id", "post.title").distinct().toJpql());
    }

    @Test
    public void testJoins() {
        assertEquals("SELECT post FROM Post post JOIN post.comments comments",
                postRepository.joins("JOIN post.comments comments").toJpql());
    }

    @Test
    public void testWhere() {
        assertEquals("SELECT post FROM Post post WHERE (post.title = 'nixon')",
                postRepository.where("post.title = 'nixon'").toJpql());
    }

    @Test
    public void testGroup() {
        assertEquals("SELECT NEW com.activepersistence.repository.models.Post(post.id, post.title) FROM Post post GROUP BY post.id, post.title",
                postRepository.select("post.id, post.title").group("post.id, post.title").toJpql());
    }

    @Test
    public void testHaving() {
        assertEquals("SELECT NEW com.activepersistence.repository.models.Post(post.id) FROM Post post GROUP BY post.id HAVING (COUNT(post.title) > 0)",
                postRepository.select("post.id").group("post.id").having("COUNT(post.title) > 0").toJpql());
    }

    @Test
    public void testOrder() {
        assertEquals("SELECT post FROM Post post ORDER BY post.id", postRepository.order("post.id").toJpql());
    }

    @Test
    public void testNone() {
        assertEquals("", postRepository.none().toJpql());
    }

    @Test
    public void testFrom() {
        assertEquals("SELECT post FROM Topic post", postRepository.from("Topic post").toJpql());
    }

    @Test
    public void testUnscope() {
        assertEquals("SELECT post FROM Post post WHERE (1=0)", postRepository.where("1=0").order("post.id").unscope(ORDER).toJpql());
    }

    @Test
    public void testUnscopeFrom() {
        assertEquals("SELECT post FROM Post post WHERE (1=0)", postRepository.where("1=0").from("Teste teste").unscope(FROM).toJpql());
    }

    @Test
    public void testReSelect() {
        assertEquals("SELECT NEW com.activepersistence.repository.models.Post(post.title) FROM Post post", postRepository.select("post.id").reselect("post.title").toJpql());
    }

    @Test
    public void testReWhere() {
        assertEquals("SELECT post FROM Post post WHERE (1=0)", postRepository.where("1=0").rewhere("1=0").toJpql());
    }

    @Test
    public void testReOrder() {
        assertEquals("SELECT post FROM Post post ORDER BY post.id", postRepository.order("post.title").reorder("post.id").toJpql());
    }

    @Test
    public void testLimit() {
        assertEquals(2, postRepository.limit(2).size());
    }

    @Test
    public void testOffset() {
        assertEquals(1, postRepository.where("post.id IN (1, 2, 3)").limit(2).offset(2).size());
    }

    @Test
    public void testIncludes() {
        var posts = postRepository.includes("post.comments");
        var comments = posts.stream().flatMap(p -> p.getComments().stream());
        assertTrue(comments.findFirst().isPresent());
    }

    @Test
    public void testEagerLoads() {
        var posts = postRepository.eagerLoad("post.comments");
        var comments = posts.stream().flatMap(p -> p.getComments().stream());
        assertTrue(comments.findFirst().isPresent());
    }

    @Test
    public void testLock() {
        assertNotNull(postRepository.lock());
    }

    @Test
    public void testUpdateReadOnly() {
        var post = postRepository.readonly().first();
        post.setTitle("change");

        assertThrows(ReadOnlyRecord.class,() -> postRepository.save(post));
    }

    @Test
    public void testDestroyReadOnly() {
        var post = postRepository.readonly().first();
        assertThrows(ReadOnlyRecord.class,() -> postRepository.destroy(post));
    }

    @Test
    public void testBind() {
        assertEquals("SELECT post FROM Post post WHERE (post.id = 1)", postRepository.where("post.id = ?", 1).toJpql());
    }

    @Test
    public void testBindNamed() {
        assertEquals("SELECT post FROM Post post WHERE (post.id = 1)", postRepository.where("post.id = :id", Map.of("id", 1)).toJpql());
    }

    @Test
    public void testBindFormat() {
        assertEquals("SELECT post FROM Post post WHERE (post.id = 1)", postRepository.where("post.id = %s", 1).toJpql());
    }
}
