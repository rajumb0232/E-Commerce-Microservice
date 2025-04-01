package com.example.user.security.jwt.secret;

import java.security.*;

import com.example.user.infrastructure.cache.CacheName;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.UUID;

@Component
@Slf4j
@AllArgsConstructor
public class SecretManager {

    private final Vault vault;
    private final CacheManager cacheManager;

    @Scheduled(initialDelay = 0, fixedDelayString = "${app.security.secret-rotate-interval-millis}")
    public void rotateKeyPair() throws Exception {
        KeyPair keyPair = generateKeyPair();
        vault.setPrivateKey(keyPair.getPrivate());

        PublicKey publicKey = keyPair.getPublic();
        String publicKeyString = encodePublicKey(publicKey);
        String key = UUID.randomUUID().toString();

        // Cache the public key and the key id in the cache.
        publishPublicKey(key, publicKeyString);

        // Store the public key along with the key id in the publicKeyPool.
        vault.addPublicKey(key, publicKey);
    }

    private void publishPublicKey(String keyId, String publicKeyString) {
        Long generatedAt = System.currentTimeMillis();

        PublicKeyMetaData publicKeyMetaData = PublicKeyMetaData.builder()
                .id(keyId)
                .generateAt(generatedAt)
                .publicKey(publicKeyString)
                .build();

        Cache cache = cacheManager.getCache(CacheName.PUBLIC_KEYS_POOL);
        if (cache != null)
            cache.put(keyId, publicKeyMetaData);
        else
            log.error("Failed to publish new Public Key, Cache: {} not found.", CacheName.PUBLIC_KEYS_POOL);
    }

    private static KeyPair generateKeyPair() throws NoSuchAlgorithmException {
        KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
        generator.initialize(2048, new SecureRandom());
        return generator.generateKeyPair();
    }

    private static String encodePublicKey(PublicKey publicKey) {
        return Base64.getEncoder().encodeToString(publicKey.getEncoded());
    }


    public PublicKey getPublicKey(String key) {
        var publicKey = vault.getPublicKey(key);
        if (publicKey == null) {
            // Not found in pool, trying to load the public key from the cache.
            var publicKeyMetaData = getFromCache(key);
            if (publicKeyMetaData != null) {
                try {
                    publicKey = decodePublicKey(publicKeyMetaData.getPublicKey());
                    // Adding the public key to the vault.
                    vault.addPublicKey(key, publicKey);
                    return publicKey;
                } catch (Exception e) {
                    log.error("Failed to decode public key with id: {}", key, e);
                }
            }
        }
        return publicKey;
    }

    private PublicKeyMetaData getFromCache(String key) {
        var cache = cacheManager.getCache(CacheName.PUBLIC_KEYS_POOL);

        if (cache != null) {
            return cache.get(key, PublicKeyMetaData.class);
        } else
            log.error("Failed to load Public Key, Cache: {} not found.", CacheName.PUBLIC_KEYS_POOL);

        return null;
    }

    private static PublicKey decodePublicKey(String publicKeyString) throws Exception {
        byte[] keyBytes = Base64.getDecoder().decode(publicKeyString);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        return keyFactory.generatePublic(new X509EncodedKeySpec(keyBytes));
    }
}