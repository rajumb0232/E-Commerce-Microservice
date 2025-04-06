package com.example.validator.jwt.secret;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PublicKeyMetaData {
    private String id;
    private Long generateAt;
    private String publicKey;
}
