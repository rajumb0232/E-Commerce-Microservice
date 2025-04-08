package com.rajugowda.jwt.validator.util;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum TokenType {
    ACCESS("at"),
    REFRESH("rt");

    private final String abbreviation;
}
