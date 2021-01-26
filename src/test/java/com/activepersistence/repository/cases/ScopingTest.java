
package com.activepersistence.repository.cases;

import com.activepersistence.IntegrationTest;
import com.activepersistence.repository.models.ClientRepository;
import com.activepersistence.repository.models.PostRepository;
import javax.inject.Inject;
import org.jboss.arquillian.persistence.UsingDataSet;
import static org.junit.Assert.assertEquals;
import org.junit.Test;

@UsingDataSet({"posts.xml", "comments.xml"})
public class ScopingTest extends IntegrationTest {

    @Inject
    private PostRepository postRepository;

    @Inject
    private ClientRepository ClientRepository;

    @Test
    public void testAll() {
        assertEquals("SELECT post FROM Post post", postRepository.all().toJpql());
    }

    @Test
    public void testUnscoped() {
        assertEquals("SELECT client FROM Client client", ClientRepository.unscoped().toJpql());
    }

    @Test
    public void testUnscopedAfter() {
        assertEquals("SELECT client FROM Client client", ClientRepository.where("1=0").unscoped().toJpql());
    }

    @Test
    public void testUnscopedBlock() {
        assertEquals("SELECT client FROM Client client WHERE (1=0)", ClientRepository.unscoped(() -> ClientRepository.where("1=0")).toJpql());
    }

    @Test
    public void testDefaultScope() {
        assertEquals("SELECT client FROM Client client WHERE (client.active = true)", ClientRepository.all().toJpql());
    }

    @Test
    public void testDefaultScopeWhere() {
        assertEquals("SELECT client FROM Client client WHERE (client.active = true) AND (1=0)", ClientRepository.where("1=0").toJpql());
    }

}
