package com.activepersistence.service.cases.relation;

import com.activepersistence.IntegrationTest;
import com.activepersistence.service.models.Post;
import com.activepersistence.service.models.PostsService;
import javax.inject.Inject;
import org.jboss.arquillian.persistence.UsingDataSet;
import static org.junit.Assert.assertNotNull;
import org.junit.Before;
import org.junit.Test;

@UsingDataSet({"posts.xml", "comments.xml"})
public class BindingsTest extends IntegrationTest {

    @Inject
    private PostsService postsService;
    
    private Post postOne;

    @Before
    public void setup() {
        postOne = postsService.find(1);
    }

    @Test
    public void testOrdinalBind() {
        assertNotNull(postsService.where("this.id = ?1").bind(1, 1));
    }

    @Test
    public void testPlaceholderBind() {
        assertNotNull(postsService.where("this.id = :id").bind("id", 1));
    }

    @Test
    public void testOrdinalBindWithModel() {
        assertNotNull(postsService.where("this.id = ?1").bind(1, postOne));
    }

    @Test
    public void testPlaceholderBindWithModel() {
        assertNotNull(postsService.where("this.id = :id").bind("id", postOne));
    }

}
