package com.example.user.security.jwt.secret;

import lombok.Getter;
import org.springframework.stereotype.Component;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * InMemory Repository for storing Private and Public Keys.
 */
@Component
@Getter
public class IssuerVault {
    private PrivateKey privateKey;
    private String currentPublicKeyId;
    private final Map<String, PublicKey> publicKeyPool = new ConcurrentHashMap<>();

    public void setNewPrivateKey(PrivateKey privateKey, String correspondingPublicKeyId) {
        this.privateKey = privateKey;
        this.currentPublicKeyId = correspondingPublicKeyId;
    }

    public void currentPublicKeyIdentifier(String keyId) {
        this.currentPublicKeyId = keyId;
    }
}

