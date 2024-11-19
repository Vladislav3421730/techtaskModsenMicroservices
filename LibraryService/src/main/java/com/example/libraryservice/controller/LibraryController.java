package com.example.libraryservice.controller;

import com.example.libraryservice.dto.BookStatusDto;
import com.example.libraryservice.exception.LibraryError;
import com.example.libraryservice.service.LibraryService;
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
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@RequestMapping("/library")
@Tag(name = "Endpoints for library management", description = "Used for working with books and book statuses")
@ApiResponses(value = {
        @ApiResponse(
                responseCode = "404",
                description = "Book not found",
                content = @Content(
                        mediaType = "application/json",
                        schema = @Schema(implementation = LibraryError.class)
                )
        ),
        @ApiResponse(
                responseCode = "403",
                description = "Something wrong with JWT Token",
                content = @Content(
                        mediaType = "application/json",
                        schema = @Schema(implementation = LibraryError.class)
                )
        ),
        @ApiResponse(
                responseCode = "401",
                description = "Error when user is not authorized",
                content = @Content(
                        mediaType = "application/json",
                        schema = @Schema(implementation = LibraryError.class)
                )
        )
})
public class LibraryController {

    LibraryService libraryService;

    @ApiResponse(
            responseCode = "200",
            description = "Book statuses successfully retrieved",
            content = @Content(
                    mediaType = "application/json",
                    array = @ArraySchema(schema = @Schema(implementation = BookStatusDto.class)) ))
    @Operation(summary = "Get all book statuses", description = "Returns a list of all book statuses in the library")
    @GetMapping(value = "/getStatuses", produces = "application/json")
    public List<BookStatusDto> findAllStatus() {
        return libraryService.findAll();
    }

    @ApiResponse(
            responseCode = "200",
            description = "Book status successfully retrieved by ID",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = BookStatusDto.class) ))
    @Operation(summary = "Get book status by ID", description = "Returns the status of a book based on its ID")
    @GetMapping(value = "/getStatus/{book_status_id}", produces = "application/json")
    public BookStatusDto findStatusById(@PathVariable Long book_status_id) {
        return libraryService.findBookStatusById(book_status_id);
    }

    @ApiResponse(
            responseCode = "200",
            description = "Book status successfully updated",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = BookStatusDto.class) ))
    @Operation(summary = "Update book status", description = "Updates the status of a book based on the data provided in the request body")
    @PutMapping(value = "/updateStatus", consumes = "application/json", produces = "application/json")
    public BookStatusDto updateBookStatus(@RequestBody BookStatusDto bookStatusDto) {
        return libraryService.updateBookStatus(bookStatusDto);
    }
}
