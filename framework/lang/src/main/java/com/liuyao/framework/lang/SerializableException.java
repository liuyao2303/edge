package com.liuyao.framework.lang;

public class SerializableException extends Exception {

    public SerializableException() {
    }

    public SerializableException(Throwable cause) {
        super(cause);
    }

    public SerializableException(String message) {
        super(message);
    }

    public SerializableException(String message, Throwable cause) {
        super(message, cause);
    }
}
