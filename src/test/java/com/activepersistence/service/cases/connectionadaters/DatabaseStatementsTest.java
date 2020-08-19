package com.activepersistence.service.cases.connectionadaters;

import com.activepersistence.IntegrationTest;
import com.activepersistence.service.models.PostsService;
import java.util.Map;
import javax.inject.Inject;
import org.jboss.arquillian.persistence.UsingDataSet;
import static org.junit.Assert.assertEquals;
import org.junit.Test;

@UsingDataSet({"posts.xml", "comments.xml"})
public class DatabaseStatementsTest extends IntegrationTest {

    @Inject
    private PostsService postsService;

    @Test
    public void testSelectAll() {
        var result = postsService.getConnection().selectAll("SELECT id, title FROM Post WHERE id = 5").get(0);
        assertEquals(5 , result[0]);
        assertEquals("flood" , result[1]);
    }

    @Test
    public void testSelectAllWithBind() {
        var result = postsService.getConnection().selectAll("SELECT id, title FROM Post WHERE id = ?1", Map.of(1, 5)).get(0);
        assertEquals(5 , result[0]);
        assertEquals("flood" , result[1]);
    }

}
