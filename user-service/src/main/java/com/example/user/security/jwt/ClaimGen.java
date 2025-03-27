package com.example.user.security.jwt;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Utility class for generating JWT claims using a simple builder pattern.
 * <p>
 * This class provides a nested {@link ClaimBuilder} to help construct a set
 * of claims in a fluent manner. The built claims map is returned as an unmodifiable map,
 * ensuring that the claims cannot be altered after creation.
 * </p>
 * <p>
 * Example usage:
 * <pre>
 * Map&lt;String, Object&gt; claims = ClaimGen.createClaimBuilder()
 *         .addClaim(ClaimGen.USERNAME, "Guru")
 *         .addClaim(ClaimGen.EMAIL, "guru@example.com")
 *         .addClaim(ClaimGen.ROLE, "ADMIN")
 *         .build();
 * </pre>
 * </p>
 */
public class ClaimGen {

    /**
     * Creates a new {@link ClaimBuilder} instance to construct JWT claims.
     *
     * @return a new instance of ClaimBuilder
     */
    public static ClaimBuilder builder() {
        return new ClaimBuilder();
    }

    /**
     * Builder class for constructing a set of JWT claims.
     * <p>
     * This builder allows adding multiple claims in a fluent manner and then builds
     * an unmodifiable map of claims that can be used when generating JWT tokens.
     * </p>
     */
    public static class ClaimBuilder {
        private final Map<String, Object> claims = new HashMap<>();

        /**
         * Adds a claim with the specified key and value.
         *
         * @param key   the claim key
         * @param value the claim value
         * @return the current ClaimBuilder instance for fluent chaining
         */
        public ClaimBuilder addClaim(String key, Object value) {
            claims.put(key, value);
            return this;
        }

        /**
         * Builds and returns an unmodifiable map of the added claims.
         *
         * @return an unmodifiable Map containing the JWT claims
         */
        public Map<String, Object> build() {
            return Collections.unmodifiableMap(claims);
        }
    }
}
