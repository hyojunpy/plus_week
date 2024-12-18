package com.example.demo.util;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class PasswordEncoderTest {
    @Test
    void passwordEncodingTest() {
        String rawPassword ="asd123";

        String encodedPassword = PasswordEncoder.encode(rawPassword);

        assertNotNull(encodedPassword);
        System.out.println("Encoded Password : " + encodedPassword);
    }

    @Test
    void isPasswordMatches() {

        String rawPassword = "asd123";
        String encodedPassword = PasswordEncoder.encode(rawPassword);
        System.out.println("Encoded Password : " + encodedPassword);

        boolean isMatch = PasswordEncoder.matches(rawPassword, encodedPassword);

        assertTrue(isMatch, "패스워드 일치");
    }
    @Test
    void isPasswordNotMatches() {

        String rawPassword = "asd123";
        String wrongPassword = "hello123";
        String encodedPassword = PasswordEncoder.encode(rawPassword);
        System.out.println("Encoded Password : " + encodedPassword);

        boolean isMatch = PasswordEncoder.matches(wrongPassword, encodedPassword);

        assertTrue(isMatch, "패스워드 불일치");
    }

}
