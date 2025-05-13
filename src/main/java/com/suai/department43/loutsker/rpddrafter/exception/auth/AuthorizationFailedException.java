package com.suai.department43.loutsker.rpddrafter.exception.auth;

public class AuthorizationFailedException extends RuntimeException {
    public AuthorizationFailedException(String login) {
        super("Authorization failed with: " + login);
    }
}
