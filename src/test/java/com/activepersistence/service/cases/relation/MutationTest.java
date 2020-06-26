package com.activepersistence.service.cases.relation;

import com.activepersistence.service.Relation;
import com.activepersistence.service.models.User;
import com.activepersistence.service.models.UsersService;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

public class MutationTest {

    private final UsersService usersService = new UsersService();

    @Test
    public void testNotMutating() {
        Relation<User> relation  = usersService.where("1=1");
        Relation<User> relation2 = relation.where("2=2");

        assertEquals("SELECT this FROM User this WHERE 1=1", relation.toJpql());
        assertEquals("SELECT this FROM User this WHERE 1=1 AND 2=2", relation2.toJpql());
    }

}
