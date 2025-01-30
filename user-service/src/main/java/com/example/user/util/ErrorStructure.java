package com.example.user.util;

import lombok.*;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ErrorStructure<T> {
    private int status;
    private String message;
    private T rootCause;
}
