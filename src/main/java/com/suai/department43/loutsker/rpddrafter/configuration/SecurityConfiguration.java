package com.suai.department43.loutsker.rpddrafter.configuration;

import com.suai.department43.loutsker.rpddrafter.service.auth.external.AuthProvider;
import com.suai.department43.loutsker.rpddrafter.service.auth.internal.JDBCUserService;
import com.suai.department43.loutsker.rpddrafter.web.filter.JwtAuthorizationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {
    private final AuthProvider authProvider;
    private final JDBCUserService userDetailsService;

    @Autowired
    public SecurityConfiguration(AuthProvider authProvider, JDBCUserService userDetailsService) {
        this.authProvider = authProvider;
        this.userDetailsService = userDetailsService;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(Customizer.withDefaults())
                .authenticationProvider(authProvider)
                .userDetailsService(userDetailsService)
                .securityMatcher("/secured/**")
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/secured/setup/**").hasAuthority("ADMIN")
                        .requestMatchers("/secured/admin/**").hasAuthority("ADMIN")
                        .requestMatchers("/secured/rpd/**").hasAnyAuthority("ADMIN", "TEACHER")
                        .requestMatchers("/secured/technical/**").hasAnyAuthority("ADMIN", "TEACHER")
                )
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(new JwtAuthorizationFilter(authProvider), UsernamePasswordAuthenticationFilter.class)
                .build();
    }
}
