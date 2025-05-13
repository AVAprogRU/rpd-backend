package com.suai.department43.loutsker.rpddrafter.exception;

import com.suai.department43.loutsker.rpddrafter.exception.auth.AuthorizationFailedException;
import com.suai.department43.loutsker.rpddrafter.exception.auth.LoginNotFoundException;
import com.suai.department43.loutsker.rpddrafter.exception.auth.TokenExpiredException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class AuthExceptionHandler {
    @ExceptionHandler(AuthorizationFailedException.class)
    public ResponseEntity<String> handleAuthorizationFailedException(AuthorizationFailedException ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ex.getMessage());
    }

    @ExceptionHandler(LoginNotFoundException.class)
    public ResponseEntity<String> handleLoginNotFoundException(LoginNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ex.getMessage());
    }

    @ExceptionHandler(TokenExpiredException.class)
    public ResponseEntity<String> handleTokenExpiredException(TokenExpiredException ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ex.getMessage());
    }
}
