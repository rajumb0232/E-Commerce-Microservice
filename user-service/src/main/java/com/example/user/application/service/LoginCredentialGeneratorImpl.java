package com.example.user.application.service;

import com.example.user.application.service.contracts.LoginCredentialGenerator;
import com.example.user.application.dto.AuthRecord;
import com.example.user.security.jwt.ClaimGen;
import com.example.user.security.jwt.TokenType;
import com.example.user.security.service.TokenGenerationServiceHelper;
import com.rajugowda.jwt.validator.util.ClaimNames;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Map;

/**
 * This class orchestrates the generation of access and refresh tokens for user authentication. <br>
 * <p>
 * The Class requires {@link AuthRecord} as input to generate the tokens. <br>
 * Uses {@link TokenGenerationServiceHelper} to handle the generation of tokens. <br>
 * The generated access and refresh tokens are stored as cookies in the {@link HttpHeaders} and returned. <br>
 */
@Service
@AllArgsConstructor
@Slf4j
public class LoginCredentialGeneratorImpl implements LoginCredentialGenerator {

    private final TokenGenerationServiceHelper tokenGenerationServiceHelper;

    @Override
    public HttpHeaders grantAccessAndRefreshTokenCookies(final AuthRecord authRecord) {
        log.info("Granting tokens to user with ID: {}", authRecord.userId());

        // Building claims for the tokens
        final Map<String, Object> claims = buildClaims(
                authRecord.username(),
                authRecord.email(),
                authRecord.role().toString()
        );

        // Generating tokens for access and refresh
        final String accessToken = tokenGenerationServiceHelper.issueTokenAsCookie(
                TokenType.ACCESS,
                claims,
                Instant.ofEpochMilli(authRecord.accessExpiration())
        );
        final String refreshToken = tokenGenerationServiceHelper.issueTokenAsCookie(
                TokenType.REFRESH,
                claims,
                Instant.ofEpochMilli(authRecord.refreshExpiration())
        );

        // Adding cookies to the headers
        final HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.SET_COOKIE, accessToken);
        headers.add(HttpHeaders.SET_COOKIE, refreshToken);

        return headers;
    }

    /**
     * Builds a map of JWT claims for access and refresh tokens.
     *
     * @param username the username claim
     * @param email    the email claim
     * @param role     the role claim
     * @return an unmodifiable map containing the claims
     */
    private Map<String, Object> buildClaims(final String username, final String email, final String role) {
        return ClaimGen.builder()
                .addClaim(ClaimNames.USERNAME, username)
                .addClaim(ClaimNames.EMAIL, email)
                .addClaim(ClaimNames.ROLE, role)
                .build();
    }

}
