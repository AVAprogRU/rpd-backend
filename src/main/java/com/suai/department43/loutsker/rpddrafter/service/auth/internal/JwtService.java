package com.suai.department43.loutsker.rpddrafter.service.auth.internal;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class JwtService {
    private final String secret;
    private final long jwtExpireTimeMs;

    public JwtService(@Value("${jwt.expiration.time.ms}") long jwtExpireTimeMs,
                      @Value("${jwt.secret}") String secret) {
        this.jwtExpireTimeMs = jwtExpireTimeMs;
        this.secret = secret;
    }

    public Authentication getAuthenticationFromToken(String token) {
        String username = getTokenBearer(token);
        List<String> roles = Jwts.parser().parseClaimsJws(token).getBody().get("roles", List.class);
        List<SimpleGrantedAuthority> authorities =
                roles.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList());
        return new UsernamePasswordAuthenticationToken(username, null, authorities);
    }

    public String getTokenBearer(String token) {
        return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody().getSubject();
    }

    public String generateTokenOnBearer(String bearerName) {
        return Jwts.builder()
                .setSubject(bearerName)
                .setIssuedAt(new Date())
                .setExpiration(new Date(new Date().getTime() + jwtExpireTimeMs))
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();
    }

    public boolean tokenIsValid(String token) {
        if (token == null || token.isEmpty()) {
            return false;
        }
        try {
            Jwts.parser().setSigningKey(secret).parseClaimsJws(token);
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    public String getJwtFromHttpRequest(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader == null) {
            return "";
        }
        if (authHeader.startsWith("Bearer ")) {
            return authHeader.substring(7);
        }
        return "";
    }
}
