package com.activepersistence.service.cases.arel;

import com.activepersistence.service.arel.Entity;
import com.activepersistence.service.arel.SelectManager;
import com.activepersistence.service.models.User;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class EntityTest {

    private Entity relation;

    @BeforeEach
    public void setup() {
        relation = new Entity(User.class, "this");
    }

    @Test
    public void testProject() {
        assertEquals("SELECT this FROM User this",
                relation.project("this").toJpql());
    }

    @Test
    public void testConstructor() {
        assertEquals("SELECT NEW com.activepersistence.service.models.User(this.id, this.name) FROM User this",
                relation.project("this.id", "this.name").constructor(true).toJpql());
    }

    @Test
    public void testDistinct() {
        assertEquals("SELECT DISTINCT this FROM User this",
                relation.project("this").distinct(true).toJpql());
    }

    @Test
    public void testWhere() {
        assertEquals("SELECT this FROM User this WHERE this.id = 1",
                relation.project("this").where("this.id = 1").toJpql());
    }

    @Test
    public void testGroup() {
        assertEquals("SELECT this.name, SUM(this.value) FROM User this GROUP BY this.name",
                relation.project("this.name, SUM(this.value)").group("this.name").toJpql());
    }

    @Test
    public void testHaving() {
        assertEquals("SELECT this.name, SUM(this.value) FROM User this GROUP BY this.name HAVING SUM(this.value) > 10",
                relation.project("this.name, SUM(this.value)").group("this.name").having("SUM(this.value) > 10").toJpql());
    }

    @Test
    public void testOrder() {
        assertEquals("SELECT this FROM User this ORDER BY this.id",
                relation.project("this").order("this.id").toJpql());
    }

    @Test
    public void testJoin() {
        assertEquals("SELECT this FROM User this JOIN e.projects p",
                relation.project("this").join("JOIN e.projects p").toJpql());
    }

    @Test
    public void testFrom() {
        assertEquals("SELECT this FROM User this",
                relation.project("this").toJpql());
    }

    @Test
    public void testFromSubQuery() {
        SelectManager subquery = new Entity(User.class, "this").project("this.id", "this.name");
        assertEquals("SELECT this FROM (SELECT this.id, this.name FROM User this) this",
                relation.project("this").from(subquery).toJpql());
    }
}
