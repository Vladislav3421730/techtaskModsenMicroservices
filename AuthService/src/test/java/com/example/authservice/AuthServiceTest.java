package com.example.authservice;

import com.example.authservice.dto.JwtResponseDto;
import com.example.authservice.dto.LoginUserDto;
import com.example.authservice.dto.RegisterUserDto;
import com.example.authservice.dto.ResponseDto;
import com.example.authservice.exception.LoginFailedException;
import com.example.authservice.exception.RegistrationFailedException;
import com.example.authservice.model.User;
import com.example.authservice.repository.UserRepository;
import com.example.authservice.service.Impl.AuthServiceImpl;
import com.example.authservice.service.UserService;
import com.example.authservice.utils.JwtTokenUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AuthServiceTest {


    @Mock
    private JwtTokenUtils jwtTokenUtils;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private AuthServiceImpl authService;

    private LoginUserDto loginUserDto;

    private User user;
    private RegisterUserDto registerUserDto;

    @BeforeEach
    void setUp() {
        loginUserDto = LoginUserDto.builder()
                .username("user")
                .password("password123")
                .build();

        registerUserDto = RegisterUserDto.builder()
                .username("user")
                .password("q1w2e3")
                .build();

        user= new User();
        user.setUsername("user");

    }


    @Test
    @DisplayName("Test createAuthToken success - valid credentials")
    void testCreateAuthToken_Success() {


        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(new UsernamePasswordAuthenticationToken("user","password"));

        when(userRepository.findByUsername(loginUserDto.getUsername())).thenReturn(Optional.of(user));

        when(jwtTokenUtils.generateToken(user)).thenReturn("valid token");

        JwtResponseDto response = authService.createAuthToken(loginUserDto);

        assertNotNull(response);
        assertEquals("valid token", response.getToken());

        verify(userRepository).findByUsername(loginUserDto.getUsername());
        verify(jwtTokenUtils).generateToken(user);
    }

    @Test
    @DisplayName("Test createAuthToken failure - invalid credentials")
    void testCreateAuthToken_Failure_InvalidCredentials() {

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(BadCredentialsException.class);

        LoginFailedException exception = assertThrows(LoginFailedException.class, () -> {
            authService.createAuthToken(loginUserDto);
        });

        assertEquals("Invalid username or password for user user", exception.getMessage());

        verify(userRepository, never()).findByUsername(loginUserDto.getUsername());
    }

    @Test
    @DisplayName("Test user registration success - user does not exist")
    void testRegistration() {

        when(userRepository.findByUsername(registerUserDto.getUsername())).thenReturn(Optional.empty());

        ResponseDto response = authService.registerUser(registerUserDto);

        assertNotNull(response);
        assertEquals("User has been saved", response.getMessage());

        verify(userRepository).findByUsername(registerUserDto.getUsername());
        verify(userService).save(registerUserDto);
    }

    @Test
    @DisplayName("Test user registration failure - user already exists")
    void testRegistration_Failed() {

        when(userRepository.findByUsername(registerUserDto.getUsername())).thenReturn(Optional.of(user));

        RegistrationFailedException exception = assertThrows(RegistrationFailedException.class, () -> {
            authService.registerUser(registerUserDto);
        });

        assertNotNull(exception);
        assertEquals(String.format("User with login %s already exist is system",
                registerUserDto.getUsername()), exception.getMessage());

        verify(userRepository).findByUsername(registerUserDto.getUsername());
        verify(userService, never()).save(registerUserDto);
    }
}