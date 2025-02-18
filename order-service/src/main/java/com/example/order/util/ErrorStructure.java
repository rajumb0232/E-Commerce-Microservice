package com.example.order.util;

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
