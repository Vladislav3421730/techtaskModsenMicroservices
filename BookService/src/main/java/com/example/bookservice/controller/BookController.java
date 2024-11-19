package com.example.bookservice.controller;

import com.example.bookservice.dto.BookDto;
import com.example.bookservice.dto.ResponseDto;
import com.example.bookservice.exception.BookError;
import com.example.bookservice.service.BookService;
import com.example.bookservice.service.FreeBookService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/book")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Tag(name = "Book Endpoints", description = "Allows performing operations for adding, updating, deleting, and searching for books")
@ApiResponses(value = {
        @ApiResponse(
                responseCode = "404",
                description = "Book not found",
                content = @Content(
                        mediaType = "application/json",
                        schema = @Schema(implementation = BookError.class)
                )
        ),
        @ApiResponse(
                responseCode = "403",
                description = "Something wrong with JWT Token",
                content = @Content(
                        mediaType = "application/json",
                        schema = @Schema(implementation = BookError.class)
                )
        ),
        @ApiResponse(
                responseCode = "401",
                description = "Error when user is not authorized",
                content = @Content(
                        mediaType = "application/json",
                        schema = @Schema(implementation = BookError.class)
                )
        )
})
public class BookController {

    BookService bookService;
    FreeBookService freeBookService;

    @ApiResponse(
            responseCode = "200",
            description = "Books retrieved successfully",
            content = @Content(
                    mediaType = "application/json",
                    array = @ArraySchema(schema = @Schema(implementation = BookDto.class))))
    @Operation(summary = "Get all books", description = "Returns a list of all books available in the system")
    @GetMapping(value = "/get", produces = "application/json")
    public List<BookDto> findAllBooks() {
        return bookService.findAll();
    }

    @ApiResponse(
            responseCode = "200",
            description = "Free books retrieved successfully",
            content = @Content(
                    mediaType = "application/json",
                    array = @ArraySchema(schema = @Schema(implementation = BookDto.class))))
    @Operation(summary = "Get all free books", description = "Returns a list of all free books available in the system")
    @GetMapping(value = "/free", produces = "application/json")
    public List<BookDto> findFreeBooks(){
        return freeBookService.findFreeBooks();
    }

    @ApiResponse(
            responseCode = "200",
            description = "Book successfully retrieved by its ID",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = BookDto.class)))
    @Operation(summary = "Get book by ID", description = "Returns a book by its unique identifier")
    @GetMapping(value = "/get/{book_id}", produces = "application/json")
    public BookDto findBookById(@PathVariable Long book_id) {
        return bookService.findById(book_id);
    }

    @ApiResponse(
            responseCode = "200",
            description = "Book successfully retrieved by its ISBN",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = BookDto.class)))
    @Operation(summary = "Get book by ISBN", description = "Returns a book by its unique ISBN number")
    @GetMapping(value = "/get/isbn/{isbn}", produces = "application/json")
    public BookDto findBookByISBN(@PathVariable String isbn) {
        return bookService.findByIsbn(isbn);
    }

    @ApiResponse(
            responseCode = "200",
            description = "Book successfully saved",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = BookDto.class)))
    @Operation(summary = "Save a new book", description = "Allows adding a new book to the system")
    @PostMapping(value = "/save", consumes = "application/json", produces = "application/json")
    public BookDto saveBook(@RequestBody BookDto bookDto) {
        return bookService.save(bookDto);
    }

    @ApiResponse(
            responseCode = "200",
            description = "Book successfully updated",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = BookDto.class)))
    @Operation(summary = "Update an existing book", description = "Allows updating the details of an existing book")
    @PutMapping(value = "/update", consumes = "application/json", produces = "application/json")
    public BookDto updateBook(@RequestBody BookDto bookDto) {
        return bookService.update(bookDto);
    }

    @ApiResponse(
            responseCode = "200",
            description = "Book successfully deleted",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ResponseDto.class)))
    @Operation(summary = "Delete a book by ID", description = "Deletes a book by its unique identifier")
    @DeleteMapping(value = "/delete/{book_id}", produces = "application/json")
    public ResponseEntity<ResponseDto> deleteBook(@PathVariable Long book_id) {
        bookService.delete(book_id);
        return ResponseEntity.ok(new ResponseDto(String.format("Book with id %d has been deleted", book_id)));
    }
}
