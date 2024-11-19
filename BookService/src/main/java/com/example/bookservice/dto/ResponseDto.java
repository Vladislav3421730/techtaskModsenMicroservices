package com.example.bookservice.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@Schema(description = "Entity for sending responses in case of a 200 status")
public class ResponseDto {

    @Schema(description = "Message")
    private String message;
}
