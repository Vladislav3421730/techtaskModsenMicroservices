package com.example.authservice.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@Schema(description = "Entity for user login")
public class LoginUserDto {

    @Schema(description = "User's username", example = "username")
    private String username;

    @Schema(description = "User's password", example = "password")
    private String password;
}
