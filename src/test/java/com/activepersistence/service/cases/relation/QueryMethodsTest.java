package com.activepersistence.service.cases.relation;

import com.activepersistence.IntegrationTest;
import com.activepersistence.PreparedStatementInvalid;
import com.activepersistence.service.models.ClientsService;
import com.activepersistence.service.models.CommentsService;
import static com.activepersistence.service.models.Gender.MALE;
import com.activepersistence.service.models.Post;
import com.activepersistence.service.models.PostsService;
import static com.activepersistence.service.relation.ValueMethods.FROM;
import static com.activepersistence.service.relation.ValueMethods.ORDER;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Map;
import javax.inject.Inject;
import org.jboss.arquillian.persistence.UsingDataSet;
import static org.junit.Assert.*;
import org.junit.Test;

@UsingDataSet({"posts.xml", "comments.xml", "clients.xml"})
public class QueryMethodsTest extends IntegrationTest {

    @Inject
    private PostsService postsService;

    @Inject
    private CommentsService commentsService;

    @Inject
    private ClientsService clientsService;

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
        assertEquals("SELECT post FROM Post post JOIN post.comments c",
                postsService.joins("JOIN post.comments c").toJpql());
    }

    @Test
    public void testJoinsAlias() {
        assertEquals("SELECT post FROM Post post INNER JOIN post.comments c",
                postsService.joins("post.comments", "c").toJpql());
        assertTrue(postsService.joins("post.comments", "c").exists());
    }

    @Test
    public void testLeftOuterJoin() {
        assertEquals("SELECT post FROM Post post LEFT OUTER JOIN post.comments c",
                postsService.leftOuterJoins("post.comments", "c").toJpql());
        assertTrue(postsService.leftOuterJoins("post.comments", "c").exists());
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
    public void testFromSubquery() {
        var subquery = clientsService.unscoped().select("client.postId");
        assertEquals("SELECT post FROM Post post, (SELECT client.postId FROM Client client) subquery", postsService.from(subquery).toJpql());
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
        var post = postsService.readonly().find(1);

        post.setTitle("changed");
        postsService.update(post);

        post = postsService.find(1);
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

    @Test
    public void testBindWrongNumberVariables() {
        assertThrows(PreparedStatementInvalid.class,() -> postsService.where("post.id = ? AND post.id = ?", 1));
    }

    @Test
    public void testBindSubQuery() {
        var subquery = postsService.where("post.title = 'flood'").select("post.id");
        assertEquals("SELECT post FROM Post post WHERE post.id IN (SELECT post.id FROM Post post WHERE post.title = 'flood')", postsService.where("post.id IN (?)", subquery).toJpql());
        assertTrue(postsService.where("post.id IN (?)", subquery).exists());
    }

    @Test
    public void testAssociate() {
        var query = commentsService.where("comment.post.id = ?", postsService.find(1));
        assertEquals("SELECT comment FROM Comment comment WHERE comment.post.id = 1", query.toJpql());
        assertTrue(query.exists());
    }

    @Test
    public void testLiteralNull() {
        assertEquals("SELECT post FROM Post post WHERE post.id = NULL", postsService.where("post.id = ?", (Object) null).toJpql());
    }

    @Test
    public void testLiteralClass() {
        assertEquals("SELECT post FROM Post post WHERE post.type = Post", postsService.where("post.type = ?", Post.class).toJpql());
    }

    @Test
    public void testLiteralEnum() {
        assertEquals("SELECT client FROM Client client WHERE client.gender = com.activepersistence.service.models.Gender.MALE", clientsService.unscoped().where("client.gender = ?", MALE).toJpql());
        assertTrue(clientsService.unscoped().where("client.gender = ?", MALE).exists());
    }

    @Test
    public void testLiteralString() {
        assertEquals("SELECT client FROM Client client WHERE client.name = 'Nixon'", clientsService.unscoped().where("client.name = ?", "Nixon").toJpql());
    }

    @Test
    public void testLiteralStringQuote() {
        assertEquals("SELECT client FROM Client client WHERE client.name = 'Ni''xon'", clientsService.unscoped().where("client.name = ?", "Ni'xon").toJpql());
    }

    @Test
    public void testLiteralStringDollar() {
        assertEquals("SELECT client FROM Client client WHERE client.name = '$Nixon'", clientsService.unscoped().where("client.name = ?", "$Nixon").toJpql());
    }

    @Test
    public void testLiteralInteger() {
        assertEquals("SELECT client FROM Client client WHERE client.id = 1234", clientsService.unscoped().where("client.id = ?", 1234).toJpql());
    }

    @Test
    public void testLiteralLong() {
        assertEquals("SELECT client FROM Client client WHERE client.id = 1234L", clientsService.unscoped().where("client.id = ?", 1234L).toJpql());
    }

    @Test
    public void testLiteralFloat() {
        assertEquals("SELECT client FROM Client client WHERE client.weight = 64.14F", clientsService.unscoped().where("client.weight = ?", 64.14F).toJpql());
        assertTrue(clientsService.unscoped().where("ABS(client.weight - ?) < 0.001", 64.14F).exists());
    }

    @Test
    public void testLiteralDouble() {
        assertEquals("SELECT client FROM Client client WHERE client.ratio = 3.14D", clientsService.unscoped().where("client.ratio = ?", 3.14D).toJpql());
        assertTrue(clientsService.unscoped().where("client.ratio = ?", 3.14D).exists());
    }

    @Test
    public void testLiteralBoolean() {
        assertEquals("SELECT client FROM Client client WHERE client.active = FALSE", clientsService.unscoped().where("client.active = ?", false).toJpql());
        assertTrue(clientsService.unscoped().where("client.active = ?", false).exists());
    }

    @Test
    public void testLiteralLocalDate() {
        assertEquals("SELECT post FROM Post post WHERE post.createdAt = {d '2020-01-04'}", postsService.where("post.createdAt = ?", LocalDate.of(2020, 01, 04)).toJpql());
        assertTrue(postsService.unscoped().where("post.createdAt = ?", LocalDate.of(2020, 01, 04)).exists());
    }

    @Test
    public void testLiteralLocalDateTime() {
        assertEquals("SELECT post FROM Post post WHERE post.createdAt = {ts '2020-01-04 00:00:00.000000000'}", postsService.where("post.createdAt = ?", LocalDateTime.of(2020, 01, 04, 0, 0, 0, 0)).toJpql());
        assertTrue(postsService.unscoped().where("post.createdAt = ?", LocalDateTime.of(2020, 01, 04, 0, 0, 0, 0)).exists());
    }

    @Test
    public void testLiteralLocalTime() {
        assertEquals("SELECT post FROM Post post WHERE post.createdAt = {t '00:00:00'}", postsService.where("post.createdAt = ?", LocalTime.of(0, 0, 0)).toJpql());
    }

    @Test
    public void testLiteralRecord() {
        assertEquals("SELECT client FROM Client client WHERE client.id = 1", clientsService.unscoped().where("client.id = ?", clientsService.first()).toJpql());
        assertTrue(clientsService.unscoped().where("client.id = ?", clientsService.first()).exists());
    }

    @Test
    public void testLiteralChar() {
        assertEquals("SELECT client FROM Client client WHERE client.active = 'S'", clientsService.unscoped().where("client.active = ?", 'S').toJpql());
    }

    @Test
    public void testLiteralBigDecimal() {
        assertEquals("SELECT client FROM Client client WHERE client.salary = 8500.25D", clientsService.unscoped().where("client.salary = ?", BigDecimal.valueOf(8500.25)).toJpql());
        assertTrue(clientsService.unscoped().where("client.salary = ?", BigDecimal.valueOf(8500.25)).exists());
    }

}
