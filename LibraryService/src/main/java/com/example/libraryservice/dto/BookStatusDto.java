package com.example.libraryservice.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Schema(description = "Entity representing the status of a book")
public class BookStatusDto {

    @Schema(description = "Unique identifier for the book status record", example = "1")
    private Long id;

    @Schema(description = "Date and time when the book was borrowed", example = "2024-11-07 10:30:00")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime borrowedAt;

    @Schema(description = "Date and time by which the book should be returned", example = "2024-11-14 10:30:00")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime dueDate;
}
