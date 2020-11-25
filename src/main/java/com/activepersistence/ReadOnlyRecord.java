package com.activepersistence;

public class ReadOnlyRecord extends ActivePersistenceError {

    public ReadOnlyRecord(String message) {
        super(message);
    }

}
