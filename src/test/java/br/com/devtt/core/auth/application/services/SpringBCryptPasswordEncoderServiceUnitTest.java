package br.com.devtt.core.auth.application.services;

import br.com.devtt.core.auth.abstractions.application.services.PasswordEncoderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SpringBCryptPasswordEncoderServiceUnitTest {
    private PasswordEncoderService service;
    private String password;

    @BeforeEach
    void setUp() {
        service = new SpringBCryptPasswordEncoderService(new BCryptPasswordEncoder());
        password = "password";
    }

    @Test
    void shouldEncodePassword() {
        assertNotNull(service.encode(password));
    }

    @Test
    void shouldMatchPassword() {
        assertTrue(service.matches(password, service.encode(password)));
    }
}