package com.activepersistence.service.models;

import com.activepersistence.service.Base;
import com.activepersistence.service.Relation;

public class ClientsService extends Base<Client, Long> {

    @Override
    public Relation<Client, Long> defaultScope() {
        return where("client.active = true");
    }

}
