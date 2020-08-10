package com.activepersistence.service.cases.arel;

import com.activepersistence.service.arel.Entity;
import com.activepersistence.service.models.Post;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;

public class AttributeTest {

    private Entity relation;

    @Before
    public void setup() {
        relation = new Entity(Post.class, "post");
    }

    @Test
    public void testEqLiteralized() {
        assertEquals("SELECT FROM Post post WHERE post.title = 'nixon'",
                relation.where(relation.get("title").eq("nixon")).toJpql());
    }

    @Test
    public void testEqNull() {
        assertEquals("SELECT FROM Post post WHERE post.title IS NULL",
                relation.where(relation.get("title").eq(null)).toJpql());
    }

    @Test
    public void testNotEqLiteralized() {
        assertEquals("SELECT FROM Post post WHERE post.title != 'nixon'",
                relation.where(relation.get("title").notEq("nixon")).toJpql());
    }

    @Test
    public void testNotEqNull() {
        assertEquals("SELECT FROM Post post WHERE post.title IS NOT NULL",
                relation.where(relation.get("title").notEq(null)).toJpql());
    }

}
