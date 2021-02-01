package com.activepersistence.service.cases;

import com.activepersistence.IntegrationTest;
import com.activepersistence.service.models.Post;
import com.activepersistence.service.models.PostService;
import java.util.List;
import javax.inject.Inject;
import org.jboss.arquillian.persistence.UsingDataSet;
import static org.junit.Assert.*;
import org.junit.Test;

@UsingDataSet({"posts.xml", "comments.xml"})
public class QueryingTest extends IntegrationTest {

    @Inject
    private PostService postService;

    @Test
    public void testFindBySql() {
        List<Post> results = postService.findBySql("SELECT id, title FROM Post WHERE id = 5").getResultList(); assertEquals("flood", results.get(0).getTitle());
    }

    @Test
    public void testFindByJpql() {
        List<Post> results = postService.findByJpql("SELECT p FROM Post p WHERE p.id = 5").getResultList(); assertEquals("flood", results.get(0).getTitle());
    }

}
