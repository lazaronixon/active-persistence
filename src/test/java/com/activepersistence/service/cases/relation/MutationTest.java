package com.activepersistence.service.cases.relation;

import com.activepersistence.IntegrationTest;
import com.activepersistence.service.Relation;
import com.activepersistence.service.models.Post;
import com.activepersistence.service.models.PostsService;
import static java.util.Arrays.asList;
import java.util.Map;
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

        assertEquals("SELECT this FROM Post this WHERE 1=1", relation.toJpql());
        assertEquals("SELECT this FROM Post this WHERE 1=1 AND 2=2", relation2.toJpql());
    }

    @Test
    public void testSelect_() {
        Relation<Post> relation  = postsService.all();
        Relation<Post> relation2 = relation.select_("foo");
        assertEquals(relation, relation2);
        assertEquals(asList("foo"), relation2.getValues().getSelectValues());
    }

    @Test
    public void testJoin_() {
        Relation<Post> relation  = postsService.all();
        Relation<Post> relation2 = relation.joins_("foo");
        assertEquals(relation, relation2);
        assertEquals(asList("foo"), relation2.getValues().getJoinsValues());
    }

    @Test
    public void testWhere_() {
        Relation<Post> relation  = postsService.all();
        Relation<Post> relation2 = relation.where_("foo");
        assertEquals(relation, relation2);
        assertEquals(asList("foo"), relation2.getValues().getWhereValues());
    }

    @Test
    public void testGroup_() {
        Relation<Post> relation  = postsService.all();
        Relation<Post> relation2 = relation.group_("foo");
        assertEquals(relation, relation2);
        assertEquals(asList("foo"), relation2.getValues().getGroupValues());
    }

    @Test
    public void testHaving_() {
        Relation<Post> relation  = postsService.all();
        Relation<Post> relation2 = relation.having_("foo");
        assertEquals(relation, relation2);
        assertEquals(asList("foo"), relation2.getValues().getHavingValues());
    }

    @Test
    public void testOrder_() {
        Relation<Post> relation  = postsService.all();
        Relation<Post> relation2 = relation.order_("foo");
        assertEquals(relation, relation2);
        assertEquals(asList("foo"), relation2.getValues().getOrderValues());
    }

    @Test
    public void testLimit_() {
        Relation<Post> relation  = postsService.all();
        Relation<Post> relation2 = relation.limit_(999);
        assertEquals(relation, relation2);
        assertEquals(999, relation2.getValues().getLimitValue());
    }

    @Test
    public void testDistinct_() {
        Relation<Post> relation  = postsService.all();
        Relation<Post> relation2 = relation.distinct_(true);
        assertEquals(relation, relation2);
        assertEquals(true, relation2.getValues().isDistinctValue());
    }

    @Test
    public void testNone_() {
        Relation<Post> relation  = postsService.all();
        Relation<Post> relation2 = relation.none_();
        assertEquals(relation, relation2);
        assertEquals(asList("1=0"), relation2.getValues().getWhereValues());
    }

    @Test
    public void testIncludes_() {
        Relation<Post> relation  = postsService.all();
        Relation<Post> relation2 = relation.includes_("foo");
        assertEquals(relation, relation2);
        assertEquals(asList("foo"), relation2.getValues().getIncludesValues());
    }

    @Test
    public void testEagerLoads_() {
        Relation<Post> relation  = postsService.all();
        Relation<Post> relation2 = relation.eagerLoads_("foo");
        assertEquals(relation, relation2);
        assertEquals(asList("foo"), relation2.getValues().getEagerLoadsValues());
    }

    @Test
    public void testLock_() {
        Relation<Post> relation  = postsService.all();
        Relation<Post> relation2 = relation.lock_(true);
        assertEquals(relation, relation2);
        assertEquals(true, relation2.getValues().isLockValue());
    }

    @Test
    public void testFrom_() {
        Relation<Post> relation  = postsService.all();
        Relation<Post> relation2 = relation.from_("foo");
        assertEquals(relation, relation2);
        assertEquals("foo", relation2.getValues().getFromClause().getValue());
    }

    @Test
    public void testBindOrdinal_() {
        Relation<Post> relation  = postsService.where("1=1");
        Relation<Post> relation2 = relation.bind_(1, "foo");
        assertEquals(relation, relation2);
        assertEquals(Map.of(1, "foo"), relation2.getValues().getOrdinalParameters());
    }

    @Test
    public void testBindNamed_() {
        Relation<Post> relation  = postsService.where("1=1");
        Relation<Post> relation2 = relation.bind_("foo", "foo");
        assertEquals(relation, relation2);
        assertEquals(Map.of("foo", "foo"), relation2.getValues().getNamedParameters());
    }

}
