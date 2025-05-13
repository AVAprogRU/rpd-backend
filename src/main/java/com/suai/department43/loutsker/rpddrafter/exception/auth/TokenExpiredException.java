package com.suai.department43.loutsker.rpddrafter.exception.auth;

public class TokenExpiredException extends RuntimeException {
    public TokenExpiredException() {
        super("Session token expired");
    }
}
