package com.suai.department43.loutsker.rpddrafter.exception.business;

public class NotificationFailedException extends RuntimeException {
    public NotificationFailedException(String email) {
        super("Failed to send notification on email: \"" + email + "\"");
    }
}
