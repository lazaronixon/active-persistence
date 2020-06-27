package com.activepersistence.service.cases;

import com.activepersistence.service.models.ClientsService;
import com.activepersistence.service.models.UsersService;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class RelationTest {

    private UsersService usersService;
    private ClientsService clientsService;

    @BeforeEach
    public void setup() {
        usersService = new UsersService();
        clientsService = new ClientsService();
    }

    @Test
    public void testScoping() {
        assertEquals("SELECT this FROM User this WHERE 1=0", usersService.oneNeZero().toJpql());
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
