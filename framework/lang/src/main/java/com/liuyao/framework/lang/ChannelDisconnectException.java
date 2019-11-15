package com.liuyao.framework.lang;

public class ChannelDisconnectException extends Exception {
    public ChannelDisconnectException() {
    }

    public ChannelDisconnectException(String message) {
        super(message);
    }

    public ChannelDisconnectException(String message, Throwable cause) {
        super(message, cause);
    }

    public ChannelDisconnectException(Throwable cause) {
        super(cause);
    }

    public ChannelDisconnectException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
