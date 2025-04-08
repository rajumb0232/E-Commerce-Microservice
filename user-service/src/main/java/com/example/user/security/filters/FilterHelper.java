package com.example.user.security.filters;

import com.example.user.domain.model.UserRole;
import com.example.user.security.jwt.TokenType;
import com.rajugowda.jwt.validator.auth.TokenParser;
import com.rajugowda.jwt.validator.util.ClaimNames;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@AllArgsConstructor
public class FilterHelper {

    private final TokenParser tokenParser;

    /**
     * Gets the access token from the request cookies.
     * <p>
     * The method iterates through the request cookies and checks if the cookie name matches the token type. If a match is found, the cookie value is returned.
     * <p>
     *
     * @param tokenType the type of token to retrieve.
     * @param cookies   the request cookies.
     * @return the token if found, otherwise null.
     */
    public String getTokenFromRequestCookies(TokenType tokenType, Cookie[] cookies) {
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(tokenType.type())) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }

    /**
     * Extracts token claims from a JWT token.
     * <p>
     * The method extracts claims from the JWT and convert it into a new instance of ExtractedTokenClaims. <br>
     * The claims are validated using the {@link Valid} annotation to ensure that all required fields are present and have valid values.
     * The method also checks if the access and refresh expiration times are valid and throws an IllegalArgumentException if they are not.
     * </p>
     *
     * @param token The JWT token from which the claims are extracted.
     * @return An instance of ExtractedTokenClaims containing the extracted claims.
     * @throws IllegalArgumentException                        if the access or refresh expiration times are invalid.
     * @throws jakarta.validation.ConstraintViolationException if the claims are invalid.
     */
    public ExtractedTokenClaims extractTokenClaims(String token) {
        Claims claims = tokenParser.parseToken(token);
        var extractedClaims = new ExtractedTokenClaims(
                claims.get(ClaimNames.USERNAME, String.class),
                claims.get(ClaimNames.EMAIL, String.class),
                UserRole.valueOf(claims.get(ClaimNames.ROLE, String.class)),
                claims.getExpiration().getTime(),
                claims.getExpiration().getTime()
        );
        return validateTokenClaims(extractedClaims);
    }

    /**
     * Validates token claims using Jakarta validation.
     * The @Valid annotation triggers validation of the claims object.
     *
     * @param claims The claims to validate
     * @return The validated claims (unchanged)
     * @throws jakarta.validation.ConstraintViolationException if validation fails
     */
    private ExtractedTokenClaims validateTokenClaims(@Valid ExtractedTokenClaims claims) {
        // no implementation because the Hibernate validates the claims in the method parameter.
        return claims;
    }

    /**
     * Updates the security context with the extracted claims. <p>
     * @param request The HTTP request containing the cookies.
     * @param claims The {@link ExtractedTokenClaims} to be used for authentication.
     */
    public void updateSecurityContext(HttpServletRequest request, ExtractedTokenClaims claims) {
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                claims.username(),
                null,
                List.of(new SimpleGrantedAuthority(claims.role().name()))
        );

        authToken.setDetails(request);
        SecurityContextHolder.getContext().setAuthentication(authToken);
    }
}
