package com.example.user.security.service;

import com.example.user.shared.config.Env;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class CookieGenerator {

    private final Env env;
    /**
     * Generates a cookie string with the specified name, value, and max age.
     *
     * @param name      the name of the cookie
     * @param value     the value of the cookie (typically a JWT token)
     * @param maxAgeSec the maximum age of the cookie in seconds
     * @return the cookie string suitable for the Set-Cookie header
     */
    public String generateCookie(String name, String value, long maxAgeSec) {
        return ResponseCookie.from(name, value)
                .maxAge(maxAgeSec)
                .httpOnly(true)
                .secure(env.getSecurity().getCookie().getSecure())
                .domain(env.getSecurity().getCookie().getDomain())
                .sameSite(env.getSecurity().getCookie().getSameSite())
                .path("/")
                .build()
                .toString();
    }
}
