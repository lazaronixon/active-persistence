
package com.activepersistence.service.cases;

import com.activepersistence.IntegrationTest;
import com.activepersistence.service.models.ClientService;
import com.activepersistence.service.models.PostService;
import javax.inject.Inject;
import org.jboss.arquillian.persistence.UsingDataSet;
import static org.junit.Assert.assertEquals;
import org.junit.Test;

@UsingDataSet({"posts.xml", "comments.xml"})
public class ScopingTest extends IntegrationTest {

    @Inject
    private PostService postService;

    @Inject
    private ClientService clientService;

    @Test
    public void testAll() {
        assertEquals("SELECT post FROM Post post", postService.all().toJpql());
    }

    @Test
    public void testUnscoped() {
        assertEquals("SELECT client FROM Client client", clientService.unscoped().toJpql());
    }

    @Test
    public void testUnscopedAfter() {
        assertEquals("SELECT client FROM Client client", clientService.where("1=0").unscoped().toJpql());
    }

    @Test
    public void testUnscopedBlock() {
        assertEquals("SELECT client FROM Client client WHERE (1=0)", clientService.unscoped(() -> clientService.where("1=0")).toJpql());
    }

    @Test
    public void testDefaultScope() {
        assertEquals("SELECT client FROM Client client WHERE (client.active = true)", clientService.all().toJpql());
    }

    @Test
    public void testDefaultScopeWhere() {
        assertEquals("SELECT client FROM Client client WHERE (client.active = true) AND (1=0)", clientService.where("1=0").toJpql());
    }

}
