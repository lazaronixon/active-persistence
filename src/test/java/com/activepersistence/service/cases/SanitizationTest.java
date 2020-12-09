package com.activepersistence.service.cases;

import com.activepersistence.IntegrationTest;
import com.activepersistence.PreparedStatementInvalid;
import com.activepersistence.service.models.ClientsService;
import static com.activepersistence.service.models.Gender.MALE;
import com.activepersistence.service.models.Post;
import com.activepersistence.service.models.PostsService;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import javax.inject.Inject;
import org.jboss.arquillian.persistence.UsingDataSet;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import org.junit.Test;

@UsingDataSet({"posts.xml", "comments.xml", "clients.xml"})
public class SanitizationTest extends IntegrationTest {

    @Inject
    private PostsService postsService;

    @Inject
    private ClientsService clientsService;

    @Test
    public void testBindWrongNumberVariables() {
        assertThrows(PreparedStatementInvalid.class, () -> postsService.sanitizeJpql("post.id = ? AND post.id = ?", 1));
    }

    @Test
    public void testBindSubQuery() {
        var subquery = postsService.where("post.title = 'flood'").select("post.id");
        assertEquals("post.id IN (SELECT post.id FROM Post post WHERE (post.title = 'flood'))", postsService.sanitizeJpql("post.id IN (?)", subquery));
    }

    @Test
    public void testAssociate() {
        var query = postsService.sanitizeJpql("comment.post.id = ?", postsService.find(1L));
        assertEquals("comment.post.id = 1L", query);
    }

    @Test
    public void testQuoteNull() {
        assertEquals("post.id = NULL", postsService.sanitizeJpql("post.id = ?", (Object) null));
    }

    @Test
    public void testQuoteClass() {
        assertEquals("post.type = Post", postsService.sanitizeJpql("post.type = ?", Post.class));
    }

    @Test
    public void testQuoteEnum() {
        assertEquals("client.gender = com.activepersistence.service.models.Gender.MALE", clientsService.sanitizeJpql("client.gender = ?", MALE));
    }

    @Test
    public void testQuoteString() {
        assertEquals("client.name = 'Nixon'", clientsService.sanitizeJpql("client.name = ?", "Nixon"));
    }

    @Test
    public void testQuoteStringQuote() {
        assertEquals("client.name = 'Ni''xon'", clientsService.sanitizeJpql("client.name = ?", "Ni'xon"));
    }

    @Test
    public void testQuoteInteger() {
        assertEquals("client.id = 1234", clientsService.sanitizeJpql("client.id = ?", 1234));
    }

    @Test
    public void testQuoteLong() {
        assertEquals("client.id = 1234L", clientsService.sanitizeJpql("client.id = ?", 1234L));
    }

    @Test
    public void testQuoteFloat() {
        assertEquals("client.weight = 64.14F", clientsService.sanitizeJpql("client.weight = ?", 64.14F));
    }

    @Test
    public void testQuoteDouble() {
        assertEquals("client.ratio = 3.14D", clientsService.sanitizeJpql("client.ratio = ?", 3.14D));
    }

    @Test
    public void testQuoteBoolean() {
        assertEquals("client.active = FALSE", clientsService.sanitizeJpql("client.active = ?", false));
    }

    @Test
    public void testQuoteLocalDate() {
        assertEquals("post.createdAt = {d '2020-01-04'}", postsService.sanitizeJpql("post.createdAt = ?", LocalDate.of(2020, 01, 04)));
    }

    @Test
    public void testQuoteLocalDateTime() {
        assertEquals("post.createdAt = {ts '2020-01-04 00:00:00.000000000'}", postsService.sanitizeJpql("post.createdAt = ?", LocalDateTime.of(2020, 01, 04, 0, 0, 0, 0)));
    }

    @Test
    public void testQuoteLocalTime() {
        assertEquals("post.createdAt = {t '00:00:00'}", postsService.sanitizeJpql("post.createdAt = ?", LocalTime.of(0, 0, 0)));
    }

    @Test
    public void testQuoteRecord() {
        assertEquals("client.id = 1L", clientsService.sanitizeJpql("client.id = ?", clientsService.first()));
    }

    @Test
    public void testQuoteBigDecimal() {
        assertEquals("client.salary = 8500.25", clientsService.sanitizeJpql("client.salary = ?", BigDecimal.valueOf(8500.25)));
    }

}
