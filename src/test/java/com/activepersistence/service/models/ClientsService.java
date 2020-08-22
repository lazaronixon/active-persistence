package com.activepersistence.service.models;

import com.activepersistence.service.Base;
import com.activepersistence.service.Relation;

public class ClientsService extends Base<Client> {

    public ClientsService() {
        super(Client.class);
    }

    @Override
    public Relation<Client> defaultScope() {
        return where("client.active = true");
    }

}
