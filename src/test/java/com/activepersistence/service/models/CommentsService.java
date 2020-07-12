package com.activepersistence.service.models;

import com.activepersistence.service.Base;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

public class CommentsService extends Base<Comment> {

    @PersistenceContext
    private EntityManager entityManager;

    public CommentsService() {
        super(Comment.class);
    }

    @Override
    public EntityManager getEntityManager() {
        return entityManager;
    }
    
}
