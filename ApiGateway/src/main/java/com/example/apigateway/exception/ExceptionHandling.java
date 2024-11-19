package com.example.apigateway.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionHandling {

    @ExceptionHandler(InvalidJwtTokenException.class)
    public ResponseEntity<TokenError> handleInvalidJwtTokenException(InvalidJwtTokenException invalidJwtTokenException){
        return new ResponseEntity<>(new TokenError(invalidJwtTokenException.getMessage()), HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(MissingBearerTokenException.class)
    public ResponseEntity<TokenError> handleMissingBearerTokenException(MissingBearerTokenException missingBearerTokenException){
        return new ResponseEntity<>(new TokenError(missingBearerTokenException.getMessage()), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<TokenError> handleException(Exception exception){
        return new ResponseEntity<>(new TokenError(exception.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
    }



}
