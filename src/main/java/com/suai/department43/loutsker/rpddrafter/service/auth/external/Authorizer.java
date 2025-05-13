package com.suai.department43.loutsker.rpddrafter.service.auth.external;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

public interface Authorizer {
    UsernamePasswordAuthenticationToken authorize(String username);
    String getJwtFromHttpRequest(HttpServletRequest request);
    boolean tokenIsValid(String token);
    String getTokenBearer(String token);
}
