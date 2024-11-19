package com.example.libraryservice.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@Schema(description = "Entity for sending responses with status 200")
public class ResponseDto {

    @Schema(description = "Message")
    private String message;
}
