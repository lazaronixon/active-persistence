package com.activepersistence.repository.models;

import com.activepersistence.repository.Base;
import com.activepersistence.repository.Relation;

public class ClientRepository extends Base<Client> {

    @Override
    public Relation<Client> defaultScope() {
        return where("client.active = true");
    }

}
