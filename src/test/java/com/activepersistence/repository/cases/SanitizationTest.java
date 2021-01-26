package com.activepersistence.repository.cases;

import com.activepersistence.IntegrationTest;
import com.activepersistence.PreparedStatementInvalid;
import com.activepersistence.repository.models.ClientRepository;
import static com.activepersistence.repository.models.Gender.MALE;
import com.activepersistence.repository.models.Post;
import com.activepersistence.repository.models.PostRepository;
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
    private PostRepository postRepository;

    @Inject
    private ClientRepository ClientRepository;

    @Test
    public void testBindWrongNumberVariables() {
        assertThrows(PreparedStatementInvalid.class, () -> postRepository.sanitizeJpql("post.id = ? AND post.id = ?", 1));
    }

    @Test
    public void testBindSubQuery() {
        var subquery = postRepository.where("post.title = 'flood'").select("post.id");
        assertEquals("post.id IN (SELECT post.id FROM Post post WHERE (post.title = 'flood'))", postRepository.sanitizeJpql("post.id IN (?)", subquery));
    }

    @Test
    public void testAssociate() {
        var query = postRepository.sanitizeJpql("comment.post.id = ?", postRepository.find(1L));
        assertEquals("comment.post.id = 1L", query);
    }

    @Test
    public void testQuoteNull() {
        assertEquals("post.id = NULL", postRepository.sanitizeJpql("post.id = ?", (Object) null));
    }

    @Test
    public void testQuoteClass() {
        assertEquals("post.type = Post", postRepository.sanitizeJpql("post.type = ?", Post.class));
    }

    @Test
    public void testQuoteEnum() {
        assertEquals("client.gender = com.activepersistence.repository.models.Gender.MALE", ClientRepository.sanitizeJpql("client.gender = ?", MALE));
    }

    @Test
    public void testQuoteString() {
        assertEquals("client.name = 'Nixon'", ClientRepository.sanitizeJpql("client.name = ?", "Nixon"));
    }

    @Test
    public void testQuoteStringQuote() {
        assertEquals("client.name = 'Ni''xon'", ClientRepository.sanitizeJpql("client.name = ?", "Ni'xon"));
    }

    @Test
    public void testQuoteInteger() {
        assertEquals("client.id = 1234", ClientRepository.sanitizeJpql("client.id = ?", 1234));
    }

    @Test
    public void testQuoteLong() {
        assertEquals("client.id = 1234L", ClientRepository.sanitizeJpql("client.id = ?", 1234L));
    }

    @Test
    public void testQuoteFloat() {
        assertEquals("client.weight = 64.14F", ClientRepository.sanitizeJpql("client.weight = ?", 64.14F));
    }

    @Test
    public void testQuoteDouble() {
        assertEquals("client.ratio = 3.14D", ClientRepository.sanitizeJpql("client.ratio = ?", 3.14D));
    }

    @Test
    public void testQuoteBoolean() {
        assertEquals("client.active = FALSE", ClientRepository.sanitizeJpql("client.active = ?", false));
    }

    @Test
    public void testQuoteLocalDate() {
        assertEquals("post.createdAt = {d '2020-01-04'}", postRepository.sanitizeJpql("post.createdAt = ?", LocalDate.of(2020, 01, 04)));
    }

    @Test
    public void testQuoteLocalDateTime() {
        assertEquals("post.createdAt = {ts '2020-01-04 00:00:00.000000000'}", postRepository.sanitizeJpql("post.createdAt = ?", LocalDateTime.of(2020, 01, 04, 0, 0, 0, 0)));
    }

    @Test
    public void testQuoteLocalTime() {
        assertEquals("post.createdAt = {t '00:00:00'}", postRepository.sanitizeJpql("post.createdAt = ?", LocalTime.of(0, 0, 0)));
    }

    @Test
    public void testQuoteRecord() {
        assertEquals("client.id = 1L", ClientRepository.sanitizeJpql("client.id = ?", ClientRepository.first()));
    }

    @Test
    public void testQuoteBigDecimal() {
        assertEquals("client.salary = 8500.25", ClientRepository.sanitizeJpql("client.salary = ?", BigDecimal.valueOf(8500.25)));
    }

}
