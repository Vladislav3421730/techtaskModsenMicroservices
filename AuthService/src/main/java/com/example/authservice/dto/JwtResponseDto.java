package com.example.authservice.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@Schema(description = "Token returned upon successful authentication")
public class JwtResponseDto {

    @Schema(description = "Token", example = "48hcjs...")
    private String token;
}
