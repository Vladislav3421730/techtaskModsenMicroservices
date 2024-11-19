package com.example.authservice.controller;

import com.example.authservice.dto.JwtResponseDto;
import com.example.authservice.dto.LoginUserDto;
import com.example.authservice.dto.RegisterUserDto;
import com.example.authservice.dto.ResponseDto;
import com.example.authservice.exception.AuthError;
import com.example.authservice.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Tag(name = "Authentication and Registration Endpoints", description = "You can log in as USER or ADMIN, or register a new user.")
public class AuthController {

    AuthService authService;

    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Token returned upon successful authentication",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = JwtResponseDto.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid login or password",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = AuthError.class)
                    )
            )
    })
    @Operation(summary = "User login", description = "Upon successful authentication, you will receive a token.")
    @PostMapping(value = "/login", consumes = "application/json", produces = "application/json")
    public ResponseEntity<JwtResponseDto> createToken(@RequestBody LoginUserDto userDto) {
        return ResponseEntity.ok(authService.createAuthToken(userDto));
    }

    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Message upon successful registration",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ResponseDto.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Registration failed, user already exists in the system",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = AuthError.class)
                    )
            )
    })
    @Operation(summary = "Register a new user")
    @PostMapping(value = "/register", consumes = "application/json", produces = "application/json")
    public ResponseEntity<ResponseDto> Registration(@RequestBody RegisterUserDto userDto) {
        return ResponseEntity.ok(authService.registerUser(userDto));
    }
}
