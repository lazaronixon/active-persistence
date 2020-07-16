package com.activepersistence.service.cases.relation;

import com.activepersistence.IntegrationTest;
import com.activepersistence.service.Relation;
import com.activepersistence.service.models.Post;
import com.activepersistence.service.models.PostsService;
import static java.util.Arrays.asList;
import javax.inject.Inject;
import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class MutationTest extends IntegrationTest {

    @Inject
    private PostsService postsService;

    @Test
    public void testNotMutating() {
        Relation<Post> relation  = postsService.where("1=1");
        Relation<Post> relation2 = relation.where("2=2");

        assertEquals("SELECT post FROM Post post WHERE 1=1", relation.toJpql());
        assertEquals("SELECT post FROM Post post WHERE 1=1 AND 2=2", relation2.toJpql());
    }

    @Test
    public void testSelect$() {
        Relation<Post> relation  = postsService.all();
        Relation<Post> relation2 = relation.select$("foo");
        assertEquals(relation, relation2);
        assertEquals(asList("foo"), relation2.getValues().getSelect());
    }

    @Test
    public void testJoin$() {
        Relation<Post> relation  = postsService.all();
        Relation<Post> relation2 = relation.joins$("foo");
        assertEquals(relation, relation2);
    }

    @Test
    public void testJoin2$() {
        Relation<Post> relation  = postsService.all();
        Relation<Post> relation2 = relation.joins$("foo", "foo");
        assertEquals(relation, relation2);
    }

    @Test
    public void testLeftOuterJoin$() {
        Relation<Post> relation  = postsService.all();
        Relation<Post> relation2 = relation.leftOuterJoins$("foo", "foo");
        assertEquals(relation, relation2);
    }

    @Test
    public void testWhere$() {
        Relation<Post> relation  = postsService.all();
        Relation<Post> relation2 = relation.where$("foo");
        assertEquals(relation, relation2);
        assertEquals(asList("foo"), relation2.getValues().getWhere());
    }

    @Test
    public void testGroup$() {
        Relation<Post> relation  = postsService.all();
        Relation<Post> relation2 = relation.group$("foo");
        assertEquals(relation, relation2);
        assertEquals(asList("foo"), relation2.getValues().getGroup());
    }

    @Test
    public void testHaving$() {
        Relation<Post> relation  = postsService.all();
        Relation<Post> relation2 = relation.having$("foo");
        assertEquals(relation, relation2);
        assertEquals(asList("foo"), relation2.getValues().getHaving());
    }

    @Test
    public void testOrder$() {
        Relation<Post> relation  = postsService.all();
        Relation<Post> relation2 = relation.order$("foo");
        assertEquals(relation, relation2);
        assertEquals(asList("foo"), relation2.getValues().getOrder());
    }

    @Test
    public void testLimit$() {
        Relation<Post> relation  = postsService.all();
        Relation<Post> relation2 = relation.limit$(999);
        assertEquals(relation, relation2);
        assertEquals(999, relation2.getValues().getLimit());
    }

    @Test
    public void testDistinct$() {
        Relation<Post> relation  = postsService.all();
        Relation<Post> relation2 = relation.distinct$(true);
        assertEquals(relation, relation2);
        assertEquals(true, relation2.getValues().isDistinct());
    }

    @Test
    public void testNone$() {
        Relation<Post> relation  = postsService.all();
        Relation<Post> relation2 = relation.none$();
        assertEquals(relation, relation2);
        assertEquals(asList("1=0"), relation2.getValues().getWhere());
    }

    @Test
    public void testIncludes$() {
        Relation<Post> relation  = postsService.all();
        Relation<Post> relation2 = relation.includes$("foo");
        assertEquals(relation, relation2);
        assertEquals(asList("foo"), relation2.getValues().getIncludes());
    }

    @Test
    public void testEagerLoads$() {
        Relation<Post> relation  = postsService.all();
        Relation<Post> relation2 = relation.eagerLoad$("foo");
        assertEquals(relation, relation2);
        assertEquals(asList("foo"), relation2.getValues().getEagerLoad());
    }

    @Test
    public void testLock$() {
        Relation<Post> relation  = postsService.all();
        Relation<Post> relation2 = relation.lock$(true);
        assertEquals(relation, relation2);
        assertEquals(true, relation2.getValues().isLock());
    }

    @Test
    public void testFrom$() {
        Relation<Post> relation  = postsService.all();
        Relation<Post> relation2 = relation.from$("foo");
        assertEquals(relation, relation2);
        assertEquals("foo", relation2.getValues().getFrom().getValue());
    }

    @Test
    public void testMerge$() {
        Relation<Post> relation  = postsService.where("1=1");
        Relation<Post> relation2 = relation.merge$(postsService.where("1=2"));
        assertEquals(relation, relation2);
        assertEquals(asList("1=1", "1=2"), relation2.getValues().getWhere());
    }

}
