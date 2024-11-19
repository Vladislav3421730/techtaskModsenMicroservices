package com.example.authservice.service;


import com.example.authservice.dto.RegisterUserDto;

public interface UserService {
    void save(RegisterUserDto registerUserDto);
}
