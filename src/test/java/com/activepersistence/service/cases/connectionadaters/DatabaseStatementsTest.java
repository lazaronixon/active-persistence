package com.activepersistence.service.cases.connectionadaters;

import com.activepersistence.IntegrationTest;
import com.activepersistence.service.models.PostsService;
import java.util.List;
import java.util.Map;
import javax.inject.Inject;
import javax.persistence.Tuple;
import org.jboss.arquillian.persistence.UsingDataSet;
import static org.junit.Assert.assertEquals;
import org.junit.Test;

@UsingDataSet({"posts.xml", "comments.xml"})
public class DatabaseStatementsTest extends IntegrationTest {

    @Inject
    private PostsService postsService;

    @Test
    public void testSelectAll() {
        var result = (Object[]) postsService.getConnection().selectAll("SELECT id, title FROM Post WHERE id = 5").get(0);
        assertEquals(5L , result[0]);
        assertEquals("flood" , result[1]);
    }

    @Test
    public void testSelectAllWithBind() {
        var result = (Object[]) postsService.getConnection().selectAll("SELECT id, title FROM Post WHERE id = ?1", Map.of(1, 5)).get(0);
        assertEquals(5L , result[0]);
        assertEquals("flood" , result[1]);
    }

}
