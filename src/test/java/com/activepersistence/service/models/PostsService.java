package com.activepersistence.service.models;

import com.activepersistence.service.Base;
import com.activepersistence.service.Relation;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

public class PostsService extends Base<Post> {

    @PersistenceContext
    private EntityManager entityManager;

    public PostsService() {
        super(Post.class);
    }

    @Override
    public EntityManager getEntityManager() {
        return entityManager;
    }

    public Relation<Post> oneEqZero() {
        return where("1=0");
    }

    public Relation<Post> twoEqZero() {
        return where("2=0");
    }

}
