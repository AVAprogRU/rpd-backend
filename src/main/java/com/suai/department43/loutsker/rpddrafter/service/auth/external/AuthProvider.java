package com.suai.department43.loutsker.rpddrafter.service.auth.external;

import com.suai.department43.loutsker.rpddrafter.exception.auth.AuthorizationFailedException;
import com.suai.department43.loutsker.rpddrafter.exception.auth.LoginNotFoundException;
import com.suai.department43.loutsker.rpddrafter.service.auth.internal.JDBCUserService;
import com.suai.department43.loutsker.rpddrafter.service.auth.internal.JwtService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class AuthProvider implements AuthenticationProvider, Authorizer, Authenticator {
    private final JDBCUserService detailsService;
    private final JwtService jwtService;

    @Autowired
    public AuthProvider(JDBCUserService detailsService, JwtService jwtService) {
        this.detailsService = detailsService;
        this.jwtService = jwtService;
    }

    @Override
    public Authentication authenticate(Authentication authentication) {
        String username = authentication.getName();
        UserDetails account;
        try {
            account = detailsService.loadUserByUsername(username);
        } catch (UsernameNotFoundException ex) {
            throw new LoginNotFoundException(username);
        }
        String rawPassword = authentication.getCredentials().toString();
        if (detailsService.passwordHashMatches(rawPassword, account.getPassword())) {
            return new UsernamePasswordAuthenticationToken(account, null);
        } else {
            throw new AuthorizationFailedException(username);
        }
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }

    public String getTokenOnAuthenticatedUser(String username) {
        return jwtService.generateTokenOnBearer(username);
    }

    @Override
    public UsernamePasswordAuthenticationToken authorize(String username) {
        UserDetails account = detailsService.loadUserByUsername(username);
        return new UsernamePasswordAuthenticationToken(account, null, account.getAuthorities());
    }

    @Override
    public String getJwtFromHttpRequest(HttpServletRequest request) {
        return jwtService.getJwtFromHttpRequest(request);
    }

    @Override
    public boolean tokenIsValid(String token) {
        return jwtService.tokenIsValid(token);
    }

    @Override
    public String getTokenBearer(String token) {
        return jwtService.getTokenBearer(token);
    }
}
