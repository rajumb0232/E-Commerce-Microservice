package com.rajugowda.jwt.validator.util;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum TokenType {
    ACCESS("at"),
    REFRESH("rt");

    private final String name;
}
