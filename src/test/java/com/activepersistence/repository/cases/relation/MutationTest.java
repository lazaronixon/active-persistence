package com.activepersistence.repository.cases.relation;

import com.activepersistence.IntegrationTest;
import com.activepersistence.repository.models.PostRepository;
import static java.util.Arrays.asList;
import javax.inject.Inject;
import javax.persistence.LockModeType;
import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class MutationTest extends IntegrationTest {

    @Inject
    private PostRepository postRepository;

    @Test
    public void testNotMutating() {
        var relation  = postRepository.where("1=1");
        var relation2 = relation.where("2=2");

        assertEquals("SELECT post FROM Post post WHERE (1=1)", relation.toJpql());
        assertEquals("SELECT post FROM Post post WHERE (1=1) AND (2=2)", relation2.toJpql());
    }

    @Test
    public void testSelect$() {
        var relation  = postRepository.all();
        var relation2 = relation.select2$("foo");
        assertEquals(relation, relation2);
        assertEquals(asList("foo"), relation2.getValues().getSelect());
    }

    @Test
    public void testJoin$() {
        var relation  = postRepository.all();
        var relation2 = relation.joins$("INNER JOIN foo");
        assertEquals(relation, relation2);
    }

    @Test
    public void testWhere$() {
        var relation  = postRepository.all();
        var relation2 = relation.where$("foo");
        assertEquals(relation, relation2);
        assertEquals(asList("(foo)"), relation2.getValues().getWhere());
    }

    @Test
    public void testGroup$() {
        var relation  = postRepository.all();
        var relation2 = relation.group$("foo");
        assertEquals(relation, relation2);
        assertEquals(asList("foo"), relation2.getValues().getGroup());
    }

    @Test
    public void testHaving$() {
        var relation  = postRepository.all();
        var relation2 = relation.having$("foo");
        assertEquals(relation, relation2);
        assertEquals(asList("(foo)"), relation2.getValues().getHaving());
    }

    @Test
    public void testOrder$() {
        var relation  = postRepository.all();
        var relation2 = relation.order$("foo");
        assertEquals(relation, relation2);
        assertEquals(asList("foo"), relation2.getValues().getOrder());
    }

    @Test
    public void testLimit$() {
        var relation  = postRepository.all();
        var relation2 = relation.limit$(999);
        assertEquals(relation, relation2);
        assertEquals(999, relation2.getValues().getLimit());
    }

    @Test
    public void testDistinct$() {
        var relation  = postRepository.all();
        var relation2 = relation.distinct$(true);
        assertEquals(relation, relation2);
        assertEquals(true, relation2.getValues().isDistinct());
    }

    @Test
    public void testIncludes$() {
        var relation  = postRepository.all();
        var relation2 = relation.includes$("foo");
        assertEquals(relation, relation2);
        assertEquals(asList("foo"), relation2.getValues().getIncludes());
    }

    @Test
    public void testEagerLoads$() {
        var relation  = postRepository.all();
        var relation2 = relation.eagerLoad$("foo");
        assertEquals(relation, relation2);
        assertEquals(asList("foo"), relation2.getValues().getEagerLoad());
    }

    @Test
    public void testLock$() {
        var relation  = postRepository.all();
        var relation2 = relation.lock$(LockModeType.WRITE);
        assertEquals(relation, relation2);
        assertEquals(LockModeType.WRITE, relation2.getValues().getLock());
    }

    @Test
    public void testFrom$() {
        var relation  = postRepository.all();
        var relation2 = relation.from$("foo");
        assertEquals(relation, relation2);
        assertEquals("foo", relation2.getValues().getFrom());
    }

    @Test
    public void testMerge$() {
        var relation  = postRepository.where("1=1");
        var relation2 = relation.merge$(postRepository.where("1=2"));
        assertEquals(relation, relation2);
        assertEquals(asList("(1=1)", "(1=2)"), relation2.getValues().getWhere());
    }

    @Test
    public void testReOrder$() {
        var relation  = postRepository.order("foo");
        var relation2 = relation.reorder$("buzz");
        assertEquals(relation, relation2);
        assertEquals(asList("buzz"), relation2.getValues().getOrder());
    }


}
