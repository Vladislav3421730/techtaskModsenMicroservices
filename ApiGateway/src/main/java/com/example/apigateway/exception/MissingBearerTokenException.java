package com.example.apigateway.exception;

public class MissingBearerTokenException extends RuntimeException {
    public MissingBearerTokenException(String message) {
        super(message);
    }
}
