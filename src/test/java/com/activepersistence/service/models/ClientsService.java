package com.activepersistence.service.models;

import com.activepersistence.service.Base;
import com.activepersistence.service.Relation;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

public class ClientsService extends Base<Client> {

    @PersistenceContext
    private EntityManager entityManager;

    public ClientsService() {
        super(Client.class);
    }

    @Override
    public EntityManager getEntityManager() {
        return entityManager;
    }

    @Override
    public Relation<Client> defaultScope() {
        return where("this.active = true");
    }

    @Override
    public boolean useDefaultScope() {
        return true;
    }

}
