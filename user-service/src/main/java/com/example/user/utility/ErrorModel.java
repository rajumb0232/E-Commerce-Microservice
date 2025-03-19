package com.example.user.utility;

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
