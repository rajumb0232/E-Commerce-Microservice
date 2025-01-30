package com.example.user.exception;

public class UserNotFoundByIdException extends RuntimeException{
    public UserNotFoundByIdException(String userNotFound) {
        super(userNotFound);
    }
}
