package com.example.bookservice.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "Book entity")
public class BookDto {

    @Schema(description = "Unique identifier of the book", example = "1")
    private Long id;

    @Schema(description = "ISBN of the book", example = "978-3-16-148410-0")
    private String isbn;

    @Schema(description = "Title of the book", example = "The Great Gatsby")
    private String name;

    @Schema(description = "Genre of the book", example = "Fiction")
    private String genre;

    @Schema(description = "Brief description of the book", example = "A novel about the American dream.")
    private String description;

    @Schema(description = "Author of the book", example = "F. Scott Fitzgerald")
    private String author;
}
