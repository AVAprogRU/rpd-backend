package com.suai.department43.loutsker.rpddrafter.service.auth.external;

import org.springframework.security.core.Authentication;

public interface Authenticator {
    Authentication authenticate(Authentication token);
    String getTokenOnAuthenticatedUser(String username);
}
