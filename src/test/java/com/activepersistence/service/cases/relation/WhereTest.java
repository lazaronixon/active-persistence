package com.activepersistence.service.cases.relation;

import com.activepersistence.service.Relation;
import com.activepersistence.service.models.User;
import com.activepersistence.service.models.UsersService;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

public class WhereTest {

    private final UsersService usersService = new UsersService();

    @Test
    public void testSelect() {
        assertEquals("SELECT NEW com.activepersistence.service.models.User(this.id, this.name) FROM User this",
                usersService.select("this.id", "this.name").toJpql());
    }

    @Test
    public void testWhere() {
        assertEquals("SELECT this FROM User this WHERE this.name = 'nixon'",
                usersService.where("this.name = 'nixon'").toJpql());
    }

    @Test
    public void testJoins() {
        assertEquals("SELECT this FROM User this JOIN this.projects p",
                usersService.joins("JOIN this.projects p").toJpql());
    }

    @Test
    public void testFromSubQuery() {
        Relation<User> subquery = usersService.select("this.id", "this.name");
        assertEquals("SELECT this FROM (SELECT this.id, this.name FROM User this) this",
                usersService.from(subquery).toJpql());
    }

}
