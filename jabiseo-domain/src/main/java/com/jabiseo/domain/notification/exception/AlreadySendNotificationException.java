package com.jabiseo.domain.notification.exception;

public class AlreadySendNotificationException extends RuntimeException {

    public AlreadySendNotificationException(String message) {
        super(message);
    }

    public AlreadySendNotificationException() {
    }
}
