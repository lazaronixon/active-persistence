package com.activepersistence;

public class ActivePersistenceError extends RuntimeException {

    public ActivePersistenceError() {
    }

    public ActivePersistenceError(String message) {
        super(message);
    }

    public ActivePersistenceError(String message, Throwable cause) {
        super(message, cause);
    }

    public ActivePersistenceError(Throwable cause) {
        super(cause);
    }

    public ActivePersistenceError(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}
