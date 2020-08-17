package com.activepersistence.service.cases;

import com.activepersistence.IntegrationTest;
import com.activepersistence.service.models.PostsService;
import java.util.Map;
import javax.inject.Inject;
import org.jboss.arquillian.persistence.UsingDataSet;
import static org.junit.Assert.*;
import org.junit.Test;

@UsingDataSet({"posts.xml", "comments.xml"})
public class QueryingTest extends IntegrationTest {

    @Inject
    private PostsService postsService;

    @Test
    public void testFindBySql() {
        assertEquals("flood", postsService.findBySql("SELECT id, title FROM Post WHERE id = 5").get(0).getTitle());
    }

    @Test
    public void testFindBySqlWithBind() {
        assertEquals("flood", postsService.findBySql("SELECT id, title FROM Post WHERE id = ?1", Map.of(1, 5)).get(0).getTitle());
    }

}
