package com.example.authenticationservice.exceptions;

public class UpdatePasswordException extends RuntimeException {
    public UpdatePasswordException(String message) {
        super(message);
    }
}