package com.example.sharedjwtvalidation.security.jwt.contracts;

import com.example.sharedjwtvalidation.security.jwt.secret.PublicKeyMetaData;

/**
 * Abstract class for locating public key metadata based on a key ID.
 * This class provides a common interface for loading metadata from a cache or vault.
 * Example:
 * <pre>
 * public class VaultTokenLocatorService extends TokenLocatorService {
 *     public PublicKeyMetaData loadPublicKeyMetaData(String keyId) {
 *         // Implement logic to load metadata from the cache or any source
 *         return null;
 *     }
 * }
 * </pre>
 */
public interface TokenLocatorService {

    /**
     * Loads public key metadata based on the provided key ID.
     * @param keyId the unique identifier for the public key metadata
     * @return the PublicKeyMetaData object if found, otherwise null
     */
    PublicKeyMetaData loadPublicKeyMetaData(String keyId);
}
