package com.example.user.security.jwt.secret;

import lombok.Builder;
import lombok.Getter;

import java.io.Serializable;

/**
 * Metadata for a public key cache.
 */
@Builder
@Getter
public class PublicKeyMetaData implements Serializable {
    private String id;
    private Long generateAt;
    private String publicKey;
}
