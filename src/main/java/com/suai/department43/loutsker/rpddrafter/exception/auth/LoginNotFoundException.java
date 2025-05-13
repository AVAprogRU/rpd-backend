package com.suai.department43.loutsker.rpddrafter.exception.auth;

public class LoginNotFoundException extends RuntimeException {
    public LoginNotFoundException(String username) {
        super("Login not found: " + username);
    }
}
