package com.activepersistence;

public class PreparedStatementInvalid extends ActivePersistenceError {

    public PreparedStatementInvalid() {
    }

    public PreparedStatementInvalid(String message) {
        super(message);
    }

    public PreparedStatementInvalid(String message, Throwable cause) {
        super(message, cause);
    }

    public PreparedStatementInvalid(Throwable cause) {
        super(cause);
    }

    public PreparedStatementInvalid(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}
