package com.activepersistence.service.cases.arel;

import com.activepersistence.service.arel.Entity;
import com.activepersistence.service.arel.SelectManager;
import com.activepersistence.service.models.User;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class SelectmanagerTest {

    private SelectManager manager;

    @BeforeEach
    public void setup() {
        manager = new SelectManager(new Entity(User.class));
    }

    @Test
    public void testProject() {
        assertEquals(manager.toJpql(), "SELECT FROM User this");
    }

    @Test
    public void testConstruct() {
        assertEquals(manager.constructor(true).toJpql(), "SELECT NEW com.activepersistence.service.models.User() FROM User this");
    }

    @Test
    public void testDistinct() {
        assertEquals(manager.distinct(true).toJpql(), "SELECT DISTINCT FROM User this");
    }

    @Test
    public void testFrom() {
        assertEquals(manager.from("Another this").toJpql(), "SELECT FROM Another this");
    }

    @Test
    public void testJoin() {
        assertEquals(manager.join("JOIN this.messages m").toJpql(), "SELECT FROM User this JOIN this.messages m");
    }

    @Test
    public void testWhere() {
        assertEquals(manager.where("this.id = 1").toJpql(), "SELECT FROM User this WHERE this.id = 1");
    }

    @Test
    public void testGroup() {
        assertEquals(manager.group("this.id, this.name").toJpql(), "SELECT FROM User this GROUP BY this.id, this.name");
    }

    @Test
    public void testHaving() {
        assertEquals(manager.having("SUM(this.ordersCount) > 1").toJpql(), "SELECT FROM User this HAVING SUM(this.ordersCount) > 1");
    }

    @Test
    public void testOrder() {
        assertEquals(manager.order("this.name").toJpql(), "SELECT FROM User this ORDER BY this.name");
    }

}
