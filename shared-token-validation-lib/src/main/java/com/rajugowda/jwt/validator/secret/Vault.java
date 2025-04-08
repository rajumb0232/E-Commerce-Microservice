package com.rajugowda.jwt.validator.secret;

import com.rajugowda.jwt.validator.contracts.PublicKeysPoolService;
import com.rajugowda.jwt.validator.exceptions.InvalidPublicKeyException;
import com.rajugowda.jwt.validator.exceptions.InvalidPublicKeyMetaDataException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
@Slf4j
public class Vault {

    private final PublicKeysPoolService publicKeysPoolService;
    private final Map<String, PublicKey> publicKeyPool = new ConcurrentHashMap<>();

    public Vault(PublicKeysPoolService publicKeysPoolService) {
        this.publicKeysPoolService = publicKeysPoolService;
    }

    /**
     * Retrieves the public keyId associated with the specified keyId ID from the Vault.
     * If the keyId is not found returns null;
     *
     * @param keyId a unique identifier for the public keyId
     * @return the PublicKey object if found, otherwise null
     */
    public PublicKey getPublicKey(String keyId) throws InvalidPublicKeyMetaDataException {
        var publicKey = publicKeyPool.get(keyId);
        if (publicKey == null) {

            log.debug("Public keyId not found in pool; attempting to decode and register from metadata.");
            var metaData = publicKeysPoolService.loadPublicKeyMetaData(keyId);

            if (metaData != null && metaData.getId() != null && metaData.getPublicKey() != null) {
                publicKey = addNewToPool(metaData);
                log.info("New PublicKey registered successfully.");
            } else {
                log.warn("PublicKey MetaData invalid or not found; failed to register new public keyId.");
                throw new InvalidPublicKeyMetaDataException("Invalid or not found PublicKeyMetaData; failed to register new public keyId.");
            }
        }

        log.debug("Public keyId retrieved from pool.");
        return publicKey;
    }

    /**
     * Registers a new public key with the specified key ID in the Vault.
     *
     * @param metaData the metadata of the public key to be registered.
     * @return the PublicKey object if registered successfully, otherwise null
     */
    private PublicKey addNewToPool(PublicKeyMetaData metaData) {
        try {
            var key = metaData.getPublicKey();
            PublicKey publicKey = decodePublicKey(key);
            addPublicKey(key, publicKey);
            return publicKey;

        } catch (Exception e) {
            log.error("Failed to decode public key with id: {}", metaData.getId());
            throw new InvalidPublicKeyException("Failed to register new public key.", e);
        }
    }

    /**
     * Decodes a Base64-encoded string representing an RSA public key into a PublicKey object.
     *
     * @param publicKeyString the Base64-encoded public key to decode
     * @return the corresponding RSA PublicKey object
     * @throws Exception if decoding fails or if the RSA KeyFactory cannot be instantiated
     */
    private static PublicKey decodePublicKey(String publicKeyString) throws Exception {
        byte[] keyBytes = Base64.getDecoder().decode(publicKeyString);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        return keyFactory.generatePublic(new X509EncodedKeySpec(keyBytes));
    }

    /**
     * Registers a new public key with the specified key ID in the Vault.
     *
     * @param key       a unique identifier for the public key.
     * @param publicKey the public key to be registered.
     */
    private void addPublicKey(String key, PublicKey publicKey) {
        log.info("Registering new public key with ID: {}", key);
        publicKeyPool.put(key, publicKey);
    }
}
