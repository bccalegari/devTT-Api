package br.com.devtt.core.application.services;

import br.com.devtt.core.abstractions.application.services.TokenService;
import br.com.devtt.core.abstractions.domain.valueobjects.Token;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class JwtTokenServiceIntTest {
    private TokenService tokenService;
    private final String EMPTY_STRING = "";

    @BeforeEach
    void setUp() {
        tokenService = new JwtTokenService();
    }

    @Test
    void shouldCreateToken() {
        assertNotNull(tokenService.create(1L, "name", "role"));
    }

    @Test
    void shouldThrowExceptionWhenCreateTokenFails() {
        assertThrows(Exception.class, () -> tokenService.create(null, null, null));
    }

    @Test
    void shouldExtractSubject() {
        Token token = tokenService.create(1L, "name", "role");
        assertEquals("1", tokenService.extractSubject(token.getValue()));
    }

    @Test
    void shouldReturnEmptyStringWhenExtractSubjectFails() {
        assertEquals(EMPTY_STRING, tokenService.extractSubject(""));
    }

    @Test
    void shouldExtractName() {
        Token token = tokenService.create(1L, "name", "role");
        assertEquals("name", tokenService.extractName(token.getValue()));
    }

    @Test
    void shouldReturnEmptyStringWhenExtractNameFails() {
        assertEquals(EMPTY_STRING, tokenService.extractName(""));
    }

    @Test
    void shouldExtractRole() {
        Token token = tokenService.create(1L, "name", "role");
        assertEquals("role", tokenService.extractRole(token.getValue()));
    }

    @Test
    void shouldReturnEmptyStringWhenExtractRoleFails() {
        assertEquals(EMPTY_STRING, tokenService.extractRole(""));
    }
}