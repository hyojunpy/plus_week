package com.example.demo.util;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class PasswordEncoderTest {
    @Test
    void passwordEncodingTest() {
        String rawPassword ="asd123";
        String encodedPassword = PasswordEncoder.encode(rawPassword);

        assertNotNull(encodedPassword, "암호화된 비밀번호는 null이 될 수 없습니다.");
        assertNotEquals(rawPassword, encodedPassword, "암호화된 비밀번호는 평문 비밀번호와 같을 수 없습니다.");
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

        assertFalse(isMatch, "패스워드 불일치");
    }

}
