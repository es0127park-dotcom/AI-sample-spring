package com.example.demo;

import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class UtilTest {
    
    @Test
    public void bcrypt_test() {
        BCryptPasswordEncoder enc = new BCryptPasswordEncoder();
        String encPassword = enc.encode("1234");
        System.out.println(encPassword);
    }
}
