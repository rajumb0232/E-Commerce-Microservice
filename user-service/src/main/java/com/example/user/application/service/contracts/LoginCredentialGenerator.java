package com.example.user.application.service.contracts;

import com.example.user.application.dto.AuthRecord;
import org.springframework.http.HttpHeaders;

/**
 * Service interface for user authentication.
 */
public interface LoginCredentialGenerator {

    /**
     * Generates HTTP headers containing JWT tokens as cookies.
     *
     * @param authRecord the authentication authRecord containing user details
     * @return HttpHeaders with the access and refresh tokens set as cookies
     */
    HttpHeaders grantAccessAndRefreshTokenCookies(AuthRecord authRecord);

}
