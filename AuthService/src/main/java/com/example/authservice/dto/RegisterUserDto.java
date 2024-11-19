package com.example.authservice.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@Schema(description = "Entity for registering a new user")
public class RegisterUserDto {

    @Schema(description = "New user's username", example = "username")
    private String username;

    @Schema(description = "New user's password", example = "password")
    private String password;
}
