
package com.activepersistence.service.cases;

import com.activepersistence.IntegrationTest;
import com.activepersistence.service.models.ClientsService;
import com.activepersistence.service.models.PostsService;
import javax.inject.Inject;
import org.jboss.arquillian.persistence.UsingDataSet;
import static org.junit.Assert.assertEquals;
import org.junit.Test;

@UsingDataSet({"posts.xml", "comments.xml"})
public class ScopingTest extends IntegrationTest {

    @Inject
    private PostsService postsService;

    @Inject
    private ClientsService clientsService;

    @Test
    public void testAll() {
        assertEquals("SELECT post FROM Post post", postsService.all().toJpql());
    }

    @Test
    public void testUnscoped() {
        assertEquals("SELECT client FROM Client client", clientsService.unscoped().toJpql());
    }

    @Test
    public void testUnscopedAfter() {
        assertEquals("SELECT client FROM Client client", clientsService.where("1=0").unscoped().toJpql());
    }

    @Test
    public void testUnscopedBlock() {
        assertEquals("SELECT client FROM Client client WHERE 1=0", clientsService.unscoped(() -> clientsService.where("1=0")).toJpql());
    }

    @Test
    public void testDefaultScope() {
        assertEquals("SELECT client FROM Client client WHERE client.active = true", clientsService.all().toJpql());
    }

    @Test
    public void testDefaultScopeWhere() {
        assertEquals("SELECT client FROM Client client WHERE client.active = true AND 1=0", clientsService.where("1=0").toJpql());
    }

}
