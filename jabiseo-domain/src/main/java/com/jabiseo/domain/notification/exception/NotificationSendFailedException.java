package com.jabiseo.domain.notification.exception;


public class NotificationSendFailedException extends RuntimeException {

    private final boolean retryable;

    public NotificationSendFailedException(String message, boolean retryable, Throwable cause) {
        super(message, cause);
        this.retryable = retryable;
    }

    public boolean isRetryable() {
        return retryable;
    }
}
