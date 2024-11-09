package com.example.authenticationservice.exceptions;

public class NoActiveSessionFoundException extends RuntimeException {
    public NoActiveSessionFoundException(String message) {
        super(message);
    }
}
