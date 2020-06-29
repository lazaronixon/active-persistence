package com.activepersistence.service.cases;

import com.activepersistence.service.models.ClientsService;
import com.activepersistence.service.models.PostsService;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;

public class RelationTest {

    private PostsService postsService;
    private ClientsService clientsService;

    @Before
    public void setup() {
        postsService = new PostsService();
        clientsService = new ClientsService();
    }

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

}
