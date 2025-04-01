package com.example.user.security.jwt.secret;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * InMemory Repository for storing Private and Public Keys.
 */
@Component
@Getter
public class Vault {
    private PrivateKey privateKey;
    private String currentPublicKeyId;
    private final Map<String, PublicKey> publicKeyPool = new ConcurrentHashMap<>();

    public void setNewPrivateKey(PrivateKey privateKey, String correspondingPublicKeyId) {
        this.privateKey = privateKey;
        this.currentPublicKeyId = correspondingPublicKeyId;
    }
    public PublicKey getPublicKey(String key) {
        return publicKeyPool.getOrDefault(key, null); // You can log missing key here
    }

    public void addPublicKey(String key, PublicKey publicKey) {
        publicKeyPool.putIfAbsent(key, publicKey); // Ensures atomicity
    }
}

