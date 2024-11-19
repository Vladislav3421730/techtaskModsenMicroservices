package com.example.libraryservice.exception;

public class BookStatusNotFoundException extends RuntimeException {
    public BookStatusNotFoundException(String message) {
        super(message);
    }
}
