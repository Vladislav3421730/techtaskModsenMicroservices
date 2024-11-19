package com.example.bookservice.exception;


import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionHandling {

    @ExceptionHandler(BookNotFoundException.class)
    public ResponseEntity<BookError> handleBookNotFoundException(BookNotFoundException bookNotFoundException){
        return new ResponseEntity<>(new BookError(bookNotFoundException.getMessage()), HttpStatus.NOT_FOUND);
    }

    @ApiResponse(
            responseCode = "500",
            description = "Server side error",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = BookError.class)
            )
    )
    @ExceptionHandler(Exception.class)
    public ResponseEntity<BookError> handleException(Exception exception){
        return new ResponseEntity<>(new BookError(exception.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
