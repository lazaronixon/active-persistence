package com.activepersistence.service.cases.relation;

import com.activepersistence.service.models.UsersService;
import com.activepersistence.service.relation.ValidUnscopingValues;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class QueryMethodsTest {

    private UsersService usersService;

    @BeforeEach
    public void setup() { usersService = new UsersService(); }

    @Test
    public void testAll() {
        assertEquals("SELECT this FROM User this", usersService.all().toJpql());
    }

    @Test
    public void testSelect() {
        assertEquals("SELECT NEW com.activepersistence.service.models.User(this.id, this.name) FROM User this",
                usersService.select("this.id", "this.name").toJpql());
    }

    @Test
    public void testDistinct() {
        assertEquals("SELECT DISTINCT NEW com.activepersistence.service.models.User(this.id, this.name) FROM User this",
                usersService.select("this.id", "this.name").distinct().toJpql());
    }

    @Test
    public void testJoins() {
        assertEquals("SELECT this FROM User this JOIN this.projects p",
                usersService.joins("JOIN this.projects p").toJpql());
    }

    @Test
    public void testWhere() {
        assertEquals("SELECT this FROM User this WHERE this.name = 'nixon'",
                usersService.where("this.name = 'nixon'").toJpql());
    }

    @Test
    public void testGroup() {
        assertEquals("SELECT NEW com.activepersistence.service.models.User(this.id, this.name) FROM User this GROUP BY this.id, this.name",
                usersService.select("this.id, this.name").group("this.id, this.name").toJpql());
    }

    @Test
    public void testHaving() {
        assertEquals("SELECT NEW com.activepersistence.service.models.User(this.id) FROM User this GROUP BY this.id HAVING COUNT(this.name) > 0",
                usersService.select("this.id").group("this.id").having("COUNT(this.name) > 0").toJpql());
    }

    @Test
    public void testOrder() {
        assertEquals("SELECT this FROM User this ORDER BY this.id", usersService.order("this.id").toJpql());
    }

    @Test
    public void testNone() {
        assertEquals("SELECT this FROM User this WHERE 1=0", usersService.none().toJpql());
    }

    @Test
    public void testFrom() {
        assertEquals("SELECT NEW com.activepersistence.service.models.User(subquery.id, subquery.name) FROM (SELECT this.id, this.name FROM User this) subquery",
                usersService.select("subquery.id, subquery.name").from("(SELECT this.id, this.name FROM User this) subquery").toJpql());
    }

    @Test
    public void testUnscope() {
        assertEquals("SELECT this FROM User this", usersService.order("this.id").unscope(ValidUnscopingValues.ORDER).toJpql());
    }

    @Test
    public void testReSelect() {
        assertEquals("SELECT NEW com.activepersistence.service.models.User(this.name) FROM User this", usersService.select("this.id").reselect("this.name").toJpql());
    }

    @Test
    public void testReWhere() {
        assertEquals("SELECT this FROM User this WHERE 1=0", usersService.where("1=0").rewhere("1=0").toJpql());
    }

    @Test
    public void testReOrder() {
        assertEquals("SELECT this FROM User this ORDER BY this.id", usersService.order("this.name").reorder("this.id").toJpql());
    }

}
