package com.activepersistence.service.models;

import com.activepersistence.service.Base;
import com.activepersistence.service.Relation;
import javax.persistence.EntityManager;

public class UsersService extends Base<User> {

    private EntityManager entityManager;

    public UsersService() {
        super(User.class);
    }

    @Override
    public EntityManager getEntityManager() {
        return entityManager;
    }

    public Relation<User> oneNeZero() {
        return where("1=0");
    }

}
