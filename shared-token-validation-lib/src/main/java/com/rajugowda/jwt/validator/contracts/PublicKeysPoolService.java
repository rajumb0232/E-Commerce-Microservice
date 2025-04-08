package com.rajugowda.jwt.validator.contracts;

import com.rajugowda.jwt.validator.secret.PublicKeyMetaData;

/**
 * This interface defines a contract for loading {@link PublicKeyMetaData} based on a given key ID.
 * <p>
 * Projects using this library must implement this interface to supply their own logic for retrieving
 * public key information—commonly from a cache, vault, or any external source that holds key data.
 * </p>
 *
 * <h2>Usage</h2>
 * <p>
 * Implementations of this interface should handle:
 * <ul>
 *   <li>Resolving the key from a local or remote store, cache, or database.</li>
 *   <li>Building or retrieving a {@link PublicKeyMetaData} object that holds the details of the public key.</li>
 *   <li>Handling fallback or error cases when the key is missing or invalid.</li>
 * </ul>
 * </p>
 *
 * <pre>
 * public class VaultTokenLocatorService implements PublicKeysPoolService {
 *     &#064;Override
 *     public PublicKeyMetaData loadPublicKeyMetaData(String keyId) {
 *         // Implement logic to load metadata, e.g., from a vault or cache
 *         return new PublicKeyMetaData(...);
 *     }
 * }
 * </pre>
 *
 * <h2>Implementation Details</h2>
 * <p>
 * An example scenario is a microservice fetching public keys from a centralized store.
 * The key <strong>id</strong> is used as a reference to locate the corresponding {@link PublicKeyMetaData}.
 * The metadata typically contains the key string, generation timestamp, and any other relevant facts 
 * needed to verify signatures.
 * </p>
 */
@FunctionalInterface
public interface PublicKeysPoolService {

    /**
     * Loads public key metadata based on the provided key ID.
     * <p>
     * The {@link PublicKeyMetaData} is an object that contains the details of the public key
     * issued or published by the authentication service after key-pair generation.
     * </p>
     *
     * @param keyId the unique identifier for the public key metadata
     * @return the {@link PublicKeyMetaData} object if successfully located; otherwise {@code null}
     */
    PublicKeyMetaData loadPublicKeyMetaData(String keyId);
}
