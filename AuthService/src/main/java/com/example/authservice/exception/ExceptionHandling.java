package com.example.authservice.exception;


import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionHandling {

    @ExceptionHandler(LoginFailedException.class)
    public ResponseEntity<AuthError> handleLoginFailedException(LoginFailedException loginFailedException){
        return new ResponseEntity<>(new AuthError(loginFailedException.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(RegistrationFailedException.class)
    public ResponseEntity<AuthError> handleRegistrationException(RegistrationFailedException registrationFailedException){
        return new ResponseEntity<>(new AuthError(registrationFailedException.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ApiResponse(
            responseCode = "500",
            description = "Server side error",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = AuthError.class)
            )
    )
    @ExceptionHandler(Exception.class)
    public ResponseEntity<AuthError> handleException(Exception exception){
        return new ResponseEntity<>(new AuthError(exception.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
