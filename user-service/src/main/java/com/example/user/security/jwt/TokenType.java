package com.example.user.security.jwt;

public enum TokenType {
    ACCESS("at"), REFRESH("rt");

    private final String name;

    TokenType(String name) {
        this.name = name;
    }

    public String type() {
        return name;
    }
}
