package br.com.devtt.core.auth.abstractions.application.services;

import br.com.devtt.core.auth.abstractions.domain.valueobjects.Token;

public interface TokenService {
    Token create(Long idUser, String name, String role);
    String extractSubject(String token);
    String extractName(String token);
    String extractRole(String token);
}