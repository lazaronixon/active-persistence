package com.activepersistence.service.cases.arel;

import com.activepersistence.service.arel.Entity;
import com.activepersistence.service.models.User;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class EntityTest {

    private Entity relation;

    @BeforeEach
    public void setup() {
        relation = new Entity(User.class);
    }

    @Test
    public void testCanProject() {
        assertEquals("SELECT this FROM User this",
                relation.project("this").toJpql());
    }

    @Test
    public void testCanDistinct() {
        assertEquals("SELECT DISTINCT this FROM User this",
                relation.project("this").distinct(true).toJpql());
    }

    @Test
    public void testCanWhere() {
        assertEquals("SELECT this FROM User this WHERE this.id = 1",
                relation.project("this").where("this.id = 1").toJpql());
    }

    @Test
    public void testCanWhereWhere() {
        assertEquals("SELECT this FROM User this WHERE this.id = 1 AND this.name = 'sebastiao'",
                relation.project("this").where("this.id = 1").where("this.name = 'sebastiao'").toJpql());
    }

    @Test
    public void testCanGroup() {
        assertEquals("SELECT this.name, SUM(this.value) FROM User this GROUP BY this.name",
                relation.project("this.name, SUM(this.value)").group("this.name").toJpql());
    }

    @Test
    public void testCanHaving() {
        assertEquals("SELECT this.name, SUM(this.value) FROM User this GROUP BY this.name HAVING SUM(this.value) > 10",
                relation.project("this.name, SUM(this.value)").group("this.name").having("SUM(this.value) > 10").toJpql());
    }

    @Test
    public void testCanOrder() {
        assertEquals("SELECT this FROM User this ORDER BY this.id",
                relation.project("this").order("this.id").toJpql());
    }

    @Test
    public void testCanOrderMultiple() {
        assertEquals("SELECT this FROM User this ORDER BY this.id, this.name",
                relation.project("this").order("this.id", "this.name").toJpql());
    }

    @Test
    public void testCanJoin() {
        assertEquals("SELECT this FROM User this JOIN e.projects p",
                relation.project("this").join("JOIN e.projects p").toJpql());
    }

    @Test
    public void testCanJoinMultiple() {
        assertEquals("SELECT this FROM User this JOIN e.projects p JOIN e.projects p2",
                relation.project("this").join("JOIN e.projects p").join("JOIN e.projects p2").toJpql());
    }

    @Test
    public void testFromEntity() {
        assertEquals("SELECT this FROM User this",
                relation.project("this").from(User.class).toJpql());
    }

    @Test
    public void testCanProjectSpecificFields() {
        assertEquals("SELECT new com.activepersistence.service.models.User(this.id, this.name) FROM User this",
                relation.project("this.id", "this.name").constructor(true).toJpql());
    }
}
