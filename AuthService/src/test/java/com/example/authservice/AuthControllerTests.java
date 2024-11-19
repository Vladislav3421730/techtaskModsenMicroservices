package com.example.authservice;

import com.example.authservice.controller.AuthController;
import com.example.authservice.dto.JwtResponseDto;
import com.example.authservice.dto.LoginUserDto;
import com.example.authservice.dto.RegisterUserDto;
import com.example.authservice.dto.ResponseDto;
import com.example.authservice.service.AuthService;
import com.example.authservice.utils.JwtTokenUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AuthController.class)
@WithMockUser
public class AuthControllerTests {

    private final static ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    private final static ObjectWriter OBJECT_WRITER = OBJECT_MAPPER.writer();

    @MockBean
    private JwtTokenUtils jwtTokenUtilsMock;

    @MockBean
    private AuthService authService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("Test login endpoint - valid user")
    void testLogin() throws Exception {
        LoginUserDto loginUserDto = LoginUserDto.builder()
                .username("user")
                .password("q1w2e3")
                .build();

        String content = OBJECT_WRITER.writeValueAsString(loginUserDto);

        when(authService.createAuthToken(loginUserDto)).thenReturn(new JwtResponseDto("valid token"));

        MockHttpServletRequestBuilder mockMvcRequestBuilders = post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(content)
                .with(csrf());

        mockMvc.perform(mockMvcRequestBuilders)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", notNullValue()))
                .andExpect(jsonPath("$.token", is("valid token")));
    }

    @Test
    @DisplayName("Test user registration - successful")
    void testRegisterUserSuccess() throws Exception {
        RegisterUserDto registerUserDto = RegisterUserDto.builder()
                .username("User")
                .password("q1w2e3")
                .build();

        String content = OBJECT_WRITER.writeValueAsString(registerUserDto);

        when(authService.registerUser(registerUserDto)).thenReturn(new ResponseDto("User has been saved"));

        mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(content)
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message", is("User has been saved")));

        verify(authService, times(1)).registerUser(registerUserDto);
    }
}
