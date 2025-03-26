package com.example.user.shared.responsewrappers;

import lombok.*;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ErrorModel<T> {
    private int status;
    private String message;
    private T rootCause;
}
