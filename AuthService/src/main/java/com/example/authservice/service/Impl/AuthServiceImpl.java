package com.example.authservice.service.Impl;

import com.example.authservice.dto.JwtResponseDto;
import com.example.authservice.dto.LoginUserDto;
import com.example.authservice.dto.RegisterUserDto;
import com.example.authservice.dto.ResponseDto;
import com.example.authservice.exception.LoginFailedException;
import com.example.authservice.exception.RegistrationFailedException;
import com.example.authservice.model.User;
import com.example.authservice.repository.UserRepository;
import com.example.authservice.service.AuthService;
import com.example.authservice.service.UserService;
import com.example.authservice.utils.JwtTokenUtils;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class AuthServiceImpl implements AuthService {

    JwtTokenUtils jwtTokenUtils;
    UserRepository userRepository;
    AuthenticationManager authenticationManager;
    UserService userService;

    @Override
    public JwtResponseDto createAuthToken(LoginUserDto userDto) {

        try {

            log.info("Attempting to authenticate user: {}", userDto.getUsername());
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userDto.getUsername(), userDto.getPassword()));
            log.info("Authentication successful for user: {}", userDto.getUsername());

        } catch (BadCredentialsException badCredentialsException) {
            log.error("Authentication failed for user {}: {}", userDto.getUsername(), badCredentialsException.getMessage());

            throw new LoginFailedException(String.format("Invalid username or password for user %s", userDto.getUsername()));
        }
        log.info("JWT token generated for user: {}", userDto.getUsername());

        User user = userRepository.findByUsername(userDto.getUsername()).get();

        return new JwtResponseDto(jwtTokenUtils.generateToken(user));
    }

    @Override
    @Transactional
    public ResponseDto registerUser(RegisterUserDto userDto) {

        log.info("Attempting to register user: {}", userDto.getUsername());
        if (userRepository.findByUsername(userDto.getUsername()).isPresent()) {
            log.error("User registration failed: User with username {} already exists.", userDto.getUsername());

            throw new RegistrationFailedException(String.format("User with login %s already exist is system", userDto.getUsername()));
        }

        userService.save(userDto);
        log.info("User successfully registered: {}", userDto.getUsername());

        return new ResponseDto("User has been saved");
    }
}
