package com.rajugowda.jwt.validator.auth.dto;


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
