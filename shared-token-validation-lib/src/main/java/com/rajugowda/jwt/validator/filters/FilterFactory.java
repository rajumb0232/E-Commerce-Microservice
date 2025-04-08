package com.rajugowda.jwt.validator.filters;

import com.rajugowda.jwt.validator.auth.Authenticator;
import com.rajugowda.jwt.validator.util.TokenType;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * the {@link FilterFactory} provides explicit and lean control over creating any
 * filter instances. Designed for developers who require
 * distinct filters to handle their authentication flow.
 *
 * <h2>Purpose</h2>
 * <p>
 * Instead of relying on automatic bean scanning and configuration, which could
 * register the filters in global context for {@link org.springframework.security.web.SecurityFilterChain} leading unexpected filtration,
 * this factory provides a clear and explicit way to create and configure
 * filters for specific use cases.
 * </p>
 *
 * <h2>Usage</h2>
 * <pre>
 * &#064;Configuration
 * public class SecurityConfig {
 *
 *     &#064;Autowired
 *     private FilterFactory filterFactory;
 *
 *     &#064;Bean
 *     public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
 *         http.addFilterBefore(
 *                  filterFactory.createJwtFilter(JwtAuthFilter.class, TokenType.ACCESS),
 *                  UsernamePasswordAuthenticationFilter.class
 *              );
 *         // ...
 *         return http.build();
 *     }
 * }
 *
 * <b>Note: </b>
 * Try not to create beans for filters, as it could register the filters in global context for SecurityFilterChain.
 * This could be problematic when filters like {@link JwtFailFastAuthFilter} will completely restrict unauthenticated
 * requests.
 * </pre>
 */
@Component
@AllArgsConstructor
public class FilterFactory {

    private final Authenticator authenticator;

    /**
     * Creates an instance of the specified {@code JwtFilter} implementation,
     * initializing it with the provided {@code TokenType} and the factory's
     * {@code Authenticator}.
     * <p>
     *
     * @param <T>         the type of {@code JwtFilter} to create
     * @param filterClass the {@code Class} object corresponding to the {@code JwtFilter}
     *                    implementation to instantiate; must implement {@code JwtFilter}
     *                    and have a public no-argument constructor
     * @param tokenType   the {@code TokenType} to set on the created filter
     * @return an instance of the specified {@code JwtFilter} implementation, initialized
     * with the provided {@code tokenType} and the factory's {@code Authenticator}
     * @throws RuntimeException if the filter cannot be instantiated or initialized
     */
    public <T extends JwtFilter> T createJwtFilter(Class<T> filterClass, TokenType tokenType) throws RuntimeException {
        try {
            T filter = filterClass.getDeclaredConstructor().newInstance();
            filter.setAuthenticator(authenticator);
            filter.setTokenType(tokenType);
            return filter;
        } catch (Exception e) {
            throw new RuntimeException("Failed to create filter: " + filterClass.getSimpleName(), e);
        }
    }


}
