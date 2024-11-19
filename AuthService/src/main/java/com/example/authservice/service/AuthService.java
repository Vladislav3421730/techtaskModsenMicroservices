package com.example.authservice.service;


import com.example.authservice.dto.JwtResponseDto;
import com.example.authservice.dto.LoginUserDto;
import com.example.authservice.dto.RegisterUserDto;
import com.example.authservice.dto.ResponseDto;

public interface AuthService {

    JwtResponseDto createAuthToken(LoginUserDto userDto);

    ResponseDto registerUser(RegisterUserDto userDto);
}

