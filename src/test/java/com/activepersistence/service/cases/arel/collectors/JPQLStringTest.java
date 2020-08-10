package com.activepersistence.service.cases.arel.collectors;

import com.activepersistence.service.arel.Entity;
import com.activepersistence.service.arel.SelectManager;
import com.activepersistence.service.arel.collectors.JPQLString;
import com.activepersistence.service.arel.nodes.BindParam;
import com.activepersistence.service.arel.nodes.SelectStatement;
import com.activepersistence.service.arel.visitors.Visitable;
import com.activepersistence.service.arel.visitors.Visitor;
import com.activepersistence.service.models.Post;
import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class JPQLStringTest {

    private final Visitor visitor = Entity.visitor;

    @Test
    public void testCompile() {
        var jpql = compile(astWithBinds());
        assertEquals("SELECT FROM Post post WHERE post.title = ?1 AND post.body = ?2", jpql);
    }

    private SelectStatement astWithBinds() {
        var entity  = new Entity(Post.class, "post");
        var manager = new SelectManager(entity);
        manager.where(entity.get("title").eq(new BindParam("hello")));
        manager.where(entity.get("body").eq(new BindParam("world")));
        return manager.getAst();
    }

    private JPQLString collect(Visitable node) {
        return visitor.accept(node, new JPQLString());
    }

    private String compile(Visitable node) {
        return collect(node).getValue();
    }

}
