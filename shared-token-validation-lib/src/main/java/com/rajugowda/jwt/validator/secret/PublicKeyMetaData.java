package com.rajugowda.jwt.validator.secret;

import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PublicKeyMetaData implements Serializable {
    private String id;
    private Long generateAt;
    private String publicKey;
}
