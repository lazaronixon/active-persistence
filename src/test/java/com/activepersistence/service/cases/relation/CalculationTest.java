package com.activepersistence.service.cases.relation;

import com.activepersistence.IntegrationTest;
import com.activepersistence.service.models.PostsService;
import static java.util.Arrays.asList;
import javax.inject.Inject;
import org.jboss.arquillian.persistence.UsingDataSet;
import static org.junit.Assert.assertEquals;
import org.junit.Test;

@UsingDataSet({"posts.xml", "comments.xml"})
public class CalculationTest extends IntegrationTest {

    @Inject
    private PostsService postsService;

    @Test
    public void testCount() {
        assertEquals(2, postsService.where("this.id IN (1, 2)").count());
    }

    @Test
    public void testCountDistinct() {
        assertEquals(1, postsService.where("this.id IN (4, 5)").distinct().count("this.title"));
    }

    @Test
    public void testMinimum() {
        assertEquals(1, postsService.minimum("this.likesCount"));
    }

    @Test
    public void testMaximum() {
        assertEquals(9999, postsService.maximum("this.likesCount"));
    }

    @Test
    public void testSum() {
        assertEquals(8L, postsService.where("this.id IN (1, 2)").distinct().sum("this.likesCount"));
    }

    @Test
    public void testPluck() {
        assertEquals(asList("flood", "flood"), postsService.where("this.id IN (4, 5)").pluck("this.title"));
    }

    @Test
    public void testIds() {
        assertEquals(asList(1, 2), postsService.where("this.id IN (1, 2)").ids());
    }

}
