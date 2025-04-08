package com.example.user.security.jwt.secret;

import java.security.*;

import com.rajugowda.jwt.validator.secret.PublicKeyMetaData;
import com.rajugowda.jwt.validator.util.CacheName;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Base64;
import java.util.UUID;

@Component
@Slf4j
@AllArgsConstructor
public class SecretManager {

    private final IssuerVault issuerVault;
    private final CacheManager cacheManager;

    /**
     * Scheduled method that generates a new RSA key pair, updates the IssuerVault with the new private key,
     * publishes the public key to a cache, and adds it to the IssuerVault's public key pool for subsequent retrieval.
     *
     * @throws Exception if the key generation or publishing process encounters an error
     */
    @Scheduled(initialDelay = 0, fixedDelayString = "${app.security.secret-rotate-interval-millis}")
    public void rotateKeyPair() throws Exception {
        log.info("Rotating Key Pair...");
        KeyPair keyPair = generateKeyPair();

        PublicKey publicKey = keyPair.getPublic();
        String publicKeyString = encodePublicKey(publicKey);
        String key = UUID.randomUUID().toString();

        // Cache the public key and the key id in the cache.
        publishPublicKey(key, publicKeyString);

        // Store the public key along with the key id in the publicKeyPool.
        issuerVault.setNewPrivateKey(keyPair.getPrivate(), key);
        issuerVault.currentPublicKeyIdentifier(key);
    }

    /**
     * Publishes the given public key string (Base64-encoded) into the PUBLIC_KEY_POOL cache
     * with an assigned key ID and the time at which it was generated.
     *
     * @param keyId           a unique identifier for the public key
     * @param publicKeyString Base64-encoded public key string
     */
    private void publishPublicKey(String keyId, String publicKeyString) {
        log.info("Publishing new Public Key: {}", keyId);
        Long generatedAt = System.currentTimeMillis();

        PublicKeyMetaData publicKeyMetaData = PublicKeyMetaData.builder()
                .id(keyId)
                .generateAt(generatedAt)
                .publicKey(publicKeyString)
                .build();

        Cache cache = cacheManager.getCache(CacheName.PUBLIC_KEY_POOL);
        if (cache != null) {
            cache.put(keyId, publicKeyMetaData);
            log.info("New Public Key published successfully.");
        }
        else {
            log.error("Failed to publish new Public Key, Cache: {} not found.", CacheName.PUBLIC_KEY_POOL);
        }
    }

    /**
     * Generates and returns a new KeyPair (private and public) using an RSA algorithm, with a key size of 2048 bits.
     *
     * @return a newly generated RSA KeyPair
     * @throws NoSuchAlgorithmException if the RSA algorithm is not available
     */
    private static KeyPair generateKeyPair() throws NoSuchAlgorithmException {
        KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
        generator.initialize(2048, new SecureRandom());
        return generator.generateKeyPair();
    }

    /**
     * Encodes the given PublicKey by converting its bytes to a Base64-encoded string.
     *
     * @param publicKey the RSA public key to be encoded
     * @return Base64-encoded string representation of the public key
     */
    private static String encodePublicKey(PublicKey publicKey) {
        return Base64.getEncoder().encodeToString(publicKey.getEncoded());
    }

}
