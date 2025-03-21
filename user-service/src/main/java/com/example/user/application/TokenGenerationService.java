package com.example.user.application;

import com.example.user.application.dto.AuthRecord;
import com.example.user.security.jwt.ClaimGen;
import com.example.user.security.jwt.TokenType;
import com.example.user.security.service.TokenGenerationServiceHelper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Map;

@Service
@AllArgsConstructor
@Slf4j
public class TokenGenerationService {

    private final TokenGenerationServiceHelper tokenGenerationServiceHelper;

    /**
     * Generates HTTP headers containing JWT tokens as cookies.
     *
     * @param authRecord the authentication authRecord containing user details
     * @return HttpHeaders with the access and refresh tokens set as cookies
     */
    public HttpHeaders getLoginCredentials(AuthRecord authRecord) {
        log.info("Granting tokens to user with ID: {}", authRecord.userId());
        HttpHeaders headers = new HttpHeaders();

        Map<String, Object> claims = createClaims(authRecord);

        String accessToken = tokenGenerationServiceHelper.issueTokenAsCookie(TokenType.ACCESS, claims, Instant.ofEpochMilli(authRecord.accessExpiration()));
        String refreshToken = tokenGenerationServiceHelper.issueTokenAsCookie(TokenType.REFRESH, claims, Instant.ofEpochMilli(authRecord.refreshExpiration()));

        // Adding cookies to the headers
        headers.add(HttpHeaders.SET_COOKIE, accessToken);
        headers.add(HttpHeaders.SET_COOKIE, refreshToken);

        return headers;
    }

    /**
     * Creates a map of claims for the JWT token.
     *
     * @param authRecord the authentication authRecord containing user details
     * @return a map of claims for the JWT token
     */
    private static Map<String, Object> createClaims(AuthRecord authRecord) {
        return ClaimGen.builder()
                .addClaim(ClaimGen.USERNAME, authRecord.username())
                .addClaim(ClaimGen.EMAIL, authRecord.email())
                .addClaim(ClaimGen.ROLE, authRecord.role())
                .build();
    }
}
