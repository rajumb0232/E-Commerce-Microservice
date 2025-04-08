package com.example.user.security.config;

import com.example.user.shared.config.Env;
import com.rajugowda.jwt.validator.filters.FilterFactory;
import com.rajugowda.jwt.validator.filters.JwtAuthFilter;
import com.rajugowda.jwt.validator.util.TokenType;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    private final AuthenticationProvider authenticationProvider;
    private final Env env;
    private final FilterFactory filterFactory;

    public SecurityConfig(
            @Qualifier("authenticationProvider") AuthenticationProvider authenticationProvider,
            Env env,
            FilterFactory filterFactory) {
        this.authenticationProvider = authenticationProvider;
        this.env = env;
        this.filterFactory = filterFactory;
    }

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http.csrf(AbstractHttpConfigurer::disable)
                .authenticationProvider(authenticationProvider)
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers(
                                env.getBaseUrl() + "/login",
                                env.getBaseUrl() + "/register",
                                "/actuator/**").permitAll()
                        .anyRequest().authenticated())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(
                        filterFactory.createJwtFilter(JwtAuthFilter.class, TokenType.ACCESS),
                        UsernamePasswordAuthenticationFilter.class)
                .build();
    }
}
