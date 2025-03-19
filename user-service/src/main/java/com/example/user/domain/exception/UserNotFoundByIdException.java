package com.example.user.domain.exception;

public class UserNotFoundByIdException extends RuntimeException{
    public UserNotFoundByIdException(String userNotFound) {
        super(userNotFound);
    }
}
