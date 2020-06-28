package com.activepersistence.service.models;

import com.activepersistence.service.Base;
import com.activepersistence.service.Relation;
import javax.persistence.EntityManager;

public class PostsService extends Base<Post> {

    private EntityManager entityManager;

    public PostsService() {
        super(Post.class);
    }

    @Override
    public EntityManager getEntityManager() {
        return entityManager;
    }

    public Relation<Post> oneNeZero() {
        return where("1=0");
    }

}
