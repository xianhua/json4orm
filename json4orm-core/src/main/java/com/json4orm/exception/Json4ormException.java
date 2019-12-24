package com.json4orm.exception;

public class Json4ormException extends Exception {

    private static final long serialVersionUID = 1L;

    public Json4ormException() {
    }

    public Json4ormException(String message) {
        super(message);
    }

    public Json4ormException(Throwable cause) {
        super(cause);
    }

    public Json4ormException(String message, Throwable cause) {
        super(message, cause);
    }

    public Json4ormException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}
