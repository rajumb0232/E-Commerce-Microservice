package com.example.user.shared;

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
