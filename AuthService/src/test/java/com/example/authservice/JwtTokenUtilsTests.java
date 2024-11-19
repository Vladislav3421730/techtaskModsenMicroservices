package com.example.authservice;

import com.example.authservice.model.User;
import com.example.authservice.utils.JwtTokenUtils;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;


public class JwtTokenUtilsTests {

    private final static JwtTokenUtils JWT_TOKEN_UTILS =new JwtTokenUtils();
    private final static String JWT_SECRET="984hg493gh0439rthr0429uruj2309yh937gc763fe87t3f89723gf";
    private static User user=new User();

    private static String  userToken;

    @BeforeAll
    static void setup(){

        user.setUsername("user");

        JWT_TOKEN_UTILS.setJwtLifetime(Duration.ofMinutes(30));
        JWT_TOKEN_UTILS.setSecret(JWT_SECRET);

        userToken= JWT_TOKEN_UTILS.generateToken(user);
    }



    @Test
    @DisplayName("Test is username equal to subject from JWT token")
    void testUsernameEqualsToSubjectFromToken(){

        assertNotNull(userToken);

        assertEquals(JWT_TOKEN_UTILS.getUsername(userToken), "user");


    }

}
