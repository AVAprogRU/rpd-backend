package com.suai.department43.loutsker.rpddrafter.web.filter;

import com.suai.department43.loutsker.rpddrafter.service.auth.external.AuthProvider;
import com.suai.department43.loutsker.rpddrafter.service.auth.external.Authorizer;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthorizationFilter extends OncePerRequestFilter {
    private final Authorizer authProvider;

    @Autowired
    public JwtAuthorizationFilter(AuthProvider authProvider) {
        this.authProvider = authProvider;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        String token = authProvider.getJwtFromHttpRequest(request);
        if (authProvider.tokenIsValid(token)) {
            String username = authProvider.getTokenBearer(token);

            UsernamePasswordAuthenticationToken authentication = authProvider.authorize(username);
            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

            SecurityContextHolder.getContext().setAuthentication(authentication);
            SecurityContext securityContext = SecurityContextHolder.getContext();
            securityContext.setAuthentication(authentication);

            System.out.println("Authorized access");
        } else {
            System.out.println("Unauthorized access");
            // The response status can be overwritten by rest controller on PROTECTED endpoints
            response.setStatus(HttpStatus.FORBIDDEN.value());
        }
        filterChain.doFilter(request, response);
    }
}
