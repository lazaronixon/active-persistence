
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
        assertEquals("SELECT this FROM Post this", postsService.all().toJpql());
    }

    @Test
    public void testWhereAll() {
        assertEquals("SELECT this FROM Post this WHERE 1=0", postsService.where("1=0").all().toJpql());
    }

    @Test
    public void testUnscoped() {
        assertEquals("SELECT this FROM Client this", clientsService.unscoped().toJpql());
    }

    @Test
    public void testUnscopedAll() {
        assertEquals("SELECT this FROM Client this", clientsService.unscoped().all().toJpql());
    }

    @Test
    public void testUnscopedAfter() {
        assertEquals("SELECT this FROM Client this", clientsService.where("1=0").unscoped().toJpql());
    }

    @Test
    public void testUnscopedBlock() {
        assertEquals("SELECT this FROM Client this WHERE 1=0", clientsService.unscoped(() -> clientsService.where("1=0")).toJpql());
    }

    @Test
    public void testDefaultScope() {
        assertEquals("SELECT this FROM Client this WHERE this.active = true", clientsService.all().toJpql());
    }

    @Test
    public void testDefaultScopeWhere() {
        assertEquals("SELECT this FROM Client this WHERE this.active = true AND 1=0", clientsService.where("1=0").toJpql());
    }

}
