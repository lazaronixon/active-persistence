package com.activepersistence.repository.cases.connectionadaters;

import com.activepersistence.IntegrationTest;
import static com.activepersistence.repository.connectionadapters.QueryType.JPQL;
import static com.activepersistence.repository.connectionadapters.QueryType.SQL;
import com.activepersistence.repository.models.Post;
import com.activepersistence.repository.models.PostRepository;
import java.util.List;
import javax.inject.Inject;
import org.jboss.arquillian.persistence.UsingDataSet;
import static org.junit.Assert.assertEquals;
import org.junit.Test;

@UsingDataSet({"posts.xml", "comments.xml"})
public class DatabaseStatementsTest extends IntegrationTest {

    @Inject
    private PostRepository postRepository;

    @Test
    public void testSelectAll() {
        List<Object[]> results = postRepository.getConnection().selectAll("SELECT id, title FROM Post WHERE id = 5", SQL).getResultList(); assertEquals(5L, results.get(0)[0]); assertEquals("flood", results.get(0)[1]);
    }

    @Test
    public void testSelectEntity() {
        List<Post> results = postRepository.getConnection().selectAll("SELECT p FROM Post p WHERE p.id = 5", JPQL).getResultList(); assertEquals(5L, (long) results.get(0).getId()); assertEquals("flood", results.get(0).getTitle());
    }

}
