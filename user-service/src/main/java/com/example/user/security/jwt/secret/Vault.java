package com.example.user.security.jwt.secret;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
@Getter
@Setter
public class Vault {
    private PrivateKey privateKey;
    private final Map<String, PublicKey> publicKeyPool = new ConcurrentHashMap<>();

    public PublicKey getPublicKey(String key) {
        return publicKeyPool.getOrDefault(key, null); // You can log missing key here
    }

    public void addPublicKey(String key, PublicKey publicKey) {
        publicKeyPool.putIfAbsent(key, publicKey); // Ensures atomicity
    }
}

