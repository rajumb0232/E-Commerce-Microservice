package com.example.user.security.jwt;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Base64;

/**
 * KeyLoader is a utility class responsible for loading RSA private keys from PEM files.
 * <p>
 * This class provides a static method to load a private key from a PEM file located at the specified filepath.
 */
public class KeyLoader {

    /**
     * Loads an RSA private key from a PEM file located at the specified filepath.
     * <p>
     * This method removes the PEM headers and whitespace, decodes the Base64 content,
     * and then generates a PrivateKey instance using PKCS#8 specification.
     * </p>
     *
     * @param filepath the path to the PEM file containing the private key.
     * @return the PrivateKey instance.
     * @throws RuntimeException if the key cannot be loaded.
     */
    public static PrivateKey loadPrivateKey(String filepath) {
        try {
            String key = new String(Files.readAllBytes(Paths.get(filepath)));
            // Removing the PEM headers and newlines
            key = key.replace("-----BEGIN PRIVATE KEY-----", "")
                    .replace("-----END PRIVATE KEY-----", "")
                    .replaceAll("\\s", "");
            byte[] keyBytes = Base64.getDecoder().decode(key);
            PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(keyBytes);
            KeyFactory kf = KeyFactory.getInstance("RSA");
            return kf.generatePrivate(spec);
        } catch (Exception e) {
            throw new RuntimeException("Failed to load private key from " + filepath, e);
        }
    }
}
