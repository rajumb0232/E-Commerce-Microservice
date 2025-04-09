package com.example.order.security;

import com.rajugowda.jwt.validator.filters.FilterFactory;
import com.rajugowda.jwt.validator.filters.JwtFailFastAuthFilter;
import com.rajugowda.jwt.validator.util.TokenType;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@AllArgsConstructor
public class SecurityConfig {

    private final FilterFactory filterFactory;

    @Bean
    @Order(1)
    public SecurityFilterChain actuatorFilterChain(HttpSecurity http) throws Exception {
        return http.csrf(AbstractHttpConfigurer::disable)
                .securityMatcher("/actuator/**")
                .authorizeHttpRequests(authorize -> authorize
                        .anyRequest().permitAll())
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .build();
    }

    @Bean
    @Order(2)
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http.csrf(AbstractHttpConfigurer::disable)
                .securityMatcher("/api/**")
                .authorizeHttpRequests(authorize -> authorize
                        .anyRequest().authenticated())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(
                        filterFactory.createJwtFilter(JwtFailFastAuthFilter.class, TokenType.ACCESS),
                        UsernamePasswordAuthenticationFilter.class)
                .build();
    }
}
