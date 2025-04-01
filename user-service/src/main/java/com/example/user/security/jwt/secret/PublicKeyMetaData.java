package com.example.user.security.jwt.secret;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class PublicKeyMetaData {
    private String id;
    private Long generateAt;
    private String publicKey;
}
