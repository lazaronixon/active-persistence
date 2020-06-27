package com.activepersistence.service.models;

import com.activepersistence.service.Base;
import javax.persistence.EntityManager;

public class ClientsService extends Base<Client> {

    private EntityManager entityManager;

    public ClientsService() {
        super(Client.class);
    }

    @Override
    public EntityManager getEntityManager() {
        return entityManager;
    }

}
