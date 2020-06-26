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
    public void testFromSubQuery() {
        Relation<User> relation = usersService.all();
        assertEquals("SELECT this FROM (SELECT this FROM User this) this",
                relation.from(relation).toJpql());
    }

}
