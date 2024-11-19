package com.example.libraryservice.exception;


import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionHandling {

    @ExceptionHandler(BookStatusNotFoundException.class)
    public ResponseEntity<LibraryError> handleBookStatusNotFoundException(BookStatusNotFoundException bookStatusNotFoundException){
        return new ResponseEntity<>(new LibraryError(bookStatusNotFoundException.getMessage()), HttpStatus.NOT_FOUND);
    }

    @ApiResponse(
            responseCode = "500",
            description = "Server side error",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = LibraryError.class)
            )
    )
    @ExceptionHandler(Exception.class)
    public ResponseEntity<LibraryError> handleException(Exception exception){
        return new ResponseEntity<>(new LibraryError(exception.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
