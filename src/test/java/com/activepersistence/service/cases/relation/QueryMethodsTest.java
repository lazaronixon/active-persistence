package com.activepersistence.service.cases.relation;

import com.activepersistence.service.models.UsersService;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

public class QueryMethodsTest {

    private final UsersService usersService = new UsersService();

    @Test
    public void testWhere() {
        assertEquals("SELECT this FROM User this WHERE this.name = 'nixon'",
                usersService.where("this.name = 'nixon'").toJpql());
    }

    @Test
    public void testOrdering() {
        assertEquals("SELECT this FROM User this ORDER BY this.id",
                usersService.order("this.id").toJpql());
    }

    @Test
    public void testSelect() {
        assertEquals("SELECT new com.activepersistence.service.models.User(this.id, this.name) FROM User this",
                usersService.select("this.id", "this.name").toJpql());
    }

}
