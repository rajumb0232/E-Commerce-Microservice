package com.example.jwt.validator.auth;


import lombok.*;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FailedAuthResponse {
    private int status;
    private String message;
    private String error;
    private String additionalInfo;
}
