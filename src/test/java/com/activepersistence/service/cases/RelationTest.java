package com.activepersistence.service.cases;

import com.activepersistence.IntegrationTest;
import com.activepersistence.service.models.ClientsService;
import com.activepersistence.service.models.PostsService;
import javax.inject.Inject;
import javax.persistence.NoResultException;
import org.jboss.arquillian.persistence.UsingDataSet;
import static org.junit.Assert.*;
import org.junit.Test;

@UsingDataSet({"posts.xml", "comments.xml", "clients.xml"})
public class RelationTest extends IntegrationTest {

    @Inject
    private PostsService postsService;

    @Inject
    private ClientsService clientsService;

    @Test
    public void testScoping() {
        assertEquals("SELECT this FROM Post this WHERE 1=0", postsService.oneNeZero().toJpql());
    }

    @Test
    public void testDefaultScope() {
        assertEquals("SELECT this FROM Client this WHERE this.active = true", clientsService.all().toJpql());
    }

    @Test
    public void testUnscoped() {
        assertEquals("SELECT this FROM Client this", clientsService.unscoped().all().toJpql());
    }

    @Test
    public void testUnscopedAfter() {
        assertEquals("SELECT this FROM Client this", clientsService.where("1=0").unscoped().toJpql());
    }

    @Test
    public void testFetchOne() {
        assertNotNull(postsService.where("this.id = 1").fetchOne());
    }

    @Test
    public void testFetchOneOrFail() {
        assertThrows(NoResultException.class, () -> postsService.where("1=0").fetchOneOrFail());
    }

    @Test
    public void testFetchExists() {
        assertTrue(postsService.where("this.id = 1").fetchExists());
    }

    @Test
    public void testFetch() {
        assertNotNull(postsService.all().fetch());
    }

    @Test
    public void testFetch_() {
        assertNotNull(postsService.all().fetch_());
    }

}
