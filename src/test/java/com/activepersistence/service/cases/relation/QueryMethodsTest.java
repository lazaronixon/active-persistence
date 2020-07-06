package com.activepersistence.service.cases.relation;

import com.activepersistence.IntegrationTest;
import com.activepersistence.service.models.ClientsService;
import com.activepersistence.service.models.Comment;
import com.activepersistence.service.models.Post;
import com.activepersistence.service.models.PostsService;
import static com.activepersistence.service.relation.QueryMethods.ValidUnscopingValues.FROM;
import static com.activepersistence.service.relation.QueryMethods.ValidUnscopingValues.ORDER;
import java.util.List;
import java.util.stream.Stream;
import javax.inject.Inject;
import org.jboss.arquillian.persistence.UsingDataSet;
import static org.junit.Assert.*;
import org.junit.Test;

@UsingDataSet({"posts.xml", "comments.xml"})
public class QueryMethodsTest extends IntegrationTest {

    @Inject
    private PostsService postsService;

    @Inject
    private ClientsService clientsService;

    @Test
    public void testSelect() {
        assertEquals("SELECT NEW com.activepersistence.service.models.Post(this.id, this.title) FROM Post this",
                postsService.select("this.id", "this.title").toJpql());
    }

    @Test
    public void testDistinct() {
        assertEquals("SELECT DISTINCT NEW com.activepersistence.service.models.Post(this.id, this.title) FROM Post this",
                postsService.select("this.id", "this.title").distinct().toJpql());
    }

    @Test
    public void testJoins() {
        assertEquals("SELECT this FROM Post this JOIN this.comments c",
                postsService.joins("JOIN this.comments c").toJpql());
    }

    @Test
    public void testWhere() {
        assertEquals("SELECT this FROM Post this WHERE this.title = 'nixon'",
                postsService.where("this.title = 'nixon'").toJpql());
    }

    @Test
    public void testGroup() {
        assertEquals("SELECT NEW com.activepersistence.service.models.Post(this.id, this.title) FROM Post this GROUP BY this.id, this.title",
                postsService.select("this.id, this.title").group("this.id, this.title").toJpql());
    }

    @Test
    public void testHaving() {
        assertEquals("SELECT NEW com.activepersistence.service.models.Post(this.id) FROM Post this GROUP BY this.id HAVING COUNT(this.title) > 0",
                postsService.select("this.id").group("this.id").having("COUNT(this.title) > 0").toJpql());
    }

    @Test
    public void testOrder() {
        assertEquals("SELECT this FROM Post this ORDER BY this.id", postsService.order("this.id").toJpql());
    }

    @Test
    public void testNone() {
        assertEquals("SELECT this FROM Post this WHERE 1=0", postsService.none().toJpql());
    }

    @Test
    public void testFrom() {
        assertEquals("SELECT this FROM Topic this", postsService.from("Topic this").toJpql());
    }

    @Test
    public void testUnscope() {
        assertEquals("SELECT this FROM Post this WHERE 1=0", postsService.where("1=0").order("this.id").unscope(ORDER).toJpql());
    }

    @Test
    public void testUnscopeFrom() {
        assertEquals("SELECT this FROM Post this WHERE 1=0", postsService.where("1=0").from("Teste this").unscope(FROM).toJpql());
    }

    @Test
    public void testOnly() {
        assertEquals("SELECT this FROM Post this ORDER BY this.id", postsService.where("1=0").order("this.id").only(ORDER).toJpql());
    }

    @Test
    public void testExcept() {
        assertEquals("SELECT this FROM Post this WHERE 1=0", postsService.where("1=0").order("this.id").except(ORDER).toJpql());
    }

    @Test
    public void testReSelect() {
        assertEquals("SELECT NEW com.activepersistence.service.models.Post(this.title) FROM Post this", postsService.select("this.id").reselect("this.title").toJpql());
    }

    @Test
    public void testReWhere() {
        assertEquals("SELECT this FROM Post this WHERE 1=0", postsService.where("1=0").rewhere("1=0").toJpql());
    }

    @Test
    public void testReOrder() {
        assertEquals("SELECT this FROM Post this ORDER BY this.id", postsService.order("this.title").reorder("this.id").toJpql());
    }

    @Test
    public void testLimit() {
        assertEquals(2, postsService.limit(2).fetch().size());
    }

    @Test
    public void testOffset() {
        assertEquals(1, postsService.where("this.id IN (1, 2, 3)").limit(2).offset(2).fetch().size());
    }

    @Test
    public void testIncludes() {
        List<Post> posts = postsService.includes("this.comments").fetch();
        Stream<Comment> comments = posts.stream().flatMap(p -> p.getComments().stream());
        assertTrue(comments.findAny().isPresent());
    }

    @Test
    public void testEagerLoads() {
        List<Post> posts = postsService.eagerLoads("this.comments").fetch();
        Stream<Comment> comments = posts.stream().flatMap(p -> p.getComments().stream());
        assertTrue(comments.findAny().isPresent());
    }

    @Test
    public void testLock() {
        assertNotNull(postsService.lock().fetchOne());
    }

    @Test
    public void testOrdinalBind() {
        assertNotNull(postsService.where("this.id = ?1").bind(1, 1));
    }

    @Test
    public void testPlaceholderBind() {
        assertNotNull(postsService.where("this.id = :id").bind("id", 1));
    }

    @Test
    public void testOrdinalBindWithModel() {
        Post postOne = postsService.find(1);
        assertNotNull(postsService.where("this.id = ?1").bind(1, postOne));
    }

    @Test
    public void testPlaceholderBindWithModel() {
        Post postOne = postsService.find(1);
        assertNotNull(postsService.where("this.id = :id").bind("id", postOne));
    }

}
