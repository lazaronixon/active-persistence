package com.activepersistence.service.cases.relation;

import com.activepersistence.IntegrationTest;
import com.activepersistence.service.models.PostsService;
import static java.util.Arrays.asList;
import java.util.HashMap;
import java.util.List;
import javax.inject.Inject;
import org.jboss.arquillian.persistence.UsingDataSet;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import org.junit.Test;

@UsingDataSet({"posts.xml", "comments.xml"})
public class CalculationTest extends IntegrationTest {

    @Inject
    private PostsService postsService;

    @Test
    public void testCount() {
        assertEquals(2L, postsService.where("post.id IN (1, 2)").count());
    }

    @Test
    public void testCountAllDistinct() {
        assertEquals(2L, postsService.where("post.id IN (4, 5)").distinct().count());
    }

    @Test
    public void testCountDistinct() {
        assertEquals(1L, postsService.where("post.id IN (4, 5)").distinct().count("post.title"));
    }

    @Test
    public void testMinimum() {
        assertEquals(1, postsService.minimum("post.likesCount"));
    }

    @Test
    public void testMaximum() {
        assertEquals(9999, postsService.maximum("post.likesCount"));
    }

    @Test
    public void testSum() {
        assertEquals(8L, postsService.where("post.id IN (1, 2)").distinct().sum("post.likesCount"));
    }

    @Test
    public void testPluck() {
        List<Object> results = postsService.where("post.id IN (4, 5)").pluck("post.title");
        assertEquals(asList("flood", "flood"), results);
    }

    @Test
    public void testPluckMultiple() {
        List<Object[]> results = postsService.where("post.id IN (4, 5)").pluck("post.id", "post.title");
        assertArrayEquals(new Object[] {4L, "flood"}, results.get(0));
        assertArrayEquals(new Object[] {5L, "flood"}, results.get(1));
    }

    @Test
    public void testIds() {
        assertEquals(asList(1L, 2L), postsService.where("post.id IN (1, 2)").ids());
    }

    @Test
    public void testCountGrouped() {
        HashMap<String, Long> result = (HashMap) postsService.where("post.id IN (3, 4, 5)").group("post.title").count();
        assertEquals(Long.valueOf(1), result.get("beautiful night"));
        assertEquals(Long.valueOf(2), result.get("flood"));
        assertEquals(2, result.size());
    }

}
