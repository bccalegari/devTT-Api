package br.com.devtt.core.abstractions.application.services;

import br.com.devtt.core.abstractions.domain.valueobjects.Token;

public interface TokenService {
    public Token create(Long idUser);
    public String extractSubject(String token);
}