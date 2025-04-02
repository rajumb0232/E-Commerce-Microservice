package com.example.order.shared;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class ErrorStructure {
    private int status;
    private String errorMessage;
}
