package com.activepersistence.service.cases.relation;

import com.activepersistence.IntegrationTest;
import com.activepersistence.service.models.PostsService;
import static com.activepersistence.service.relation.ValueMethods.FROM;
import static com.activepersistence.service.relation.ValueMethods.ORDER;
import java.util.Map;
import javax.inject.Inject;
import org.jboss.arquillian.persistence.UsingDataSet;
import static org.junit.Assert.*;
import org.junit.Test;

@UsingDataSet({"posts.xml", "comments.xml", "clients.xml"})
public class QueryMethodsTest extends IntegrationTest {

    @Inject
    private PostsService postsService;

    @Test
    public void testSelect() {
        assertEquals("SELECT NEW com.activepersistence.service.models.Post(post.id, post.title) FROM Post post",
                postsService.select("post.id", "post.title").toJpql());
    }

    @Test
    public void testDistinct() {
        assertEquals("SELECT DISTINCT NEW com.activepersistence.service.models.Post(post.id, post.title) FROM Post post",
                postsService.select("post.id", "post.title").distinct().toJpql());
    }

    @Test
    public void testJoins() {
        assertEquals("SELECT post FROM Post post JOIN post.comments comments",
                postsService.joins("JOIN post.comments comments").toJpql());
    }

    @Test
    public void testWhere() {
        assertEquals("SELECT post FROM Post post WHERE post.title = 'nixon'",
                postsService.where("post.title = 'nixon'").toJpql());
    }

    @Test
    public void testGroup() {
        assertEquals("SELECT NEW com.activepersistence.service.models.Post(post.id, post.title) FROM Post post GROUP BY post.id, post.title",
                postsService.select("post.id, post.title").group("post.id, post.title").toJpql());
    }

    @Test
    public void testHaving() {
        assertEquals("SELECT NEW com.activepersistence.service.models.Post(post.id) FROM Post post GROUP BY post.id HAVING COUNT(post.title) > 0",
                postsService.select("post.id").group("post.id").having("COUNT(post.title) > 0").toJpql());
    }

    @Test
    public void testOrder() {
        assertEquals("SELECT post FROM Post post ORDER BY post.id", postsService.order("post.id").toJpql());
    }

    @Test
    public void testNone() {
        assertEquals("", postsService.none().toJpql());
    }

    @Test
    public void testFrom() {
        assertEquals("SELECT post FROM Topic post", postsService.from("Topic post").toJpql());
    }

    @Test
    public void testUnscope() {
        assertEquals("SELECT post FROM Post post WHERE 1=0", postsService.where("1=0").order("post.id").unscope(ORDER).toJpql());
    }

    @Test
    public void testUnscopeFrom() {
        assertEquals("SELECT post FROM Post post WHERE 1=0", postsService.where("1=0").from("Teste teste").unscope(FROM).toJpql());
    }

    @Test
    public void testReSelect() {
        assertEquals("SELECT NEW com.activepersistence.service.models.Post(post.title) FROM Post post", postsService.select("post.id").reselect("post.title").toJpql());
    }

    @Test
    public void testReWhere() {
        assertEquals("SELECT post FROM Post post WHERE 1=0", postsService.where("1=0").rewhere("1=0").toJpql());
    }

    @Test
    public void testReOrder() {
        assertEquals("SELECT post FROM Post post ORDER BY post.id", postsService.order("post.title").reorder("post.id").toJpql());
    }

    @Test
    public void testLimit() {
        assertEquals(2, postsService.limit(2).fetch().size());
    }

    @Test
    public void testOffset() {
        assertEquals(1, postsService.where("post.id IN (1, 2, 3)").limit(2).offset(2).fetch().size());
    }

    @Test
    public void testIncludes() {
        var posts = postsService.includes("post.comments").fetch();
        var comments = posts.stream().flatMap(p -> p.getComments().stream());
        assertTrue(comments.findAny().isPresent());
    }

    @Test
    public void testEagerLoads() {
        var posts = postsService.eagerLoad("post.comments").fetch();
        var comments = posts.stream().flatMap(p -> p.getComments().stream());
        assertTrue(comments.findAny().isPresent());
    }

    @Test
    public void testLock() {
        assertNotNull(postsService.lock().fetchOne());
    }

    @Test
    public void testReadOnly() {
        var post = postsService.readonly().find(1L);

        post.setTitle("changed");
        postsService.update(post);

        post = postsService.find(1L);
        postsService.reload(post);

        assertNotEquals("changed", post.getTitle());
    }

    @Test
    public void testBind() {
        assertEquals("SELECT post FROM Post post WHERE post.id = 1", postsService.where("post.id = ?", 1).toJpql());
    }

    @Test
    public void testBindNamed() {
        assertEquals("SELECT post FROM Post post WHERE post.id = 1", postsService.where("post.id = :id", Map.of("id", 1)).toJpql());
    }

    @Test
    public void testBindFormat() {
        assertEquals("SELECT post FROM Post post WHERE post.id = 1", postsService.where("post.id = %s", 1).toJpql());
    }
}
