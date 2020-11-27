package com.activepersistence;

import javax.persistence.PersistenceException;

public class ActivePersistenceError extends PersistenceException {

    public ActivePersistenceError(String message) {
        super(message);
    }

}
