package br.com.devTT.core.abstractions.application.services;

import br.com.devTT.core.abstractions.domain.valueobjects.Token;

public interface TokenService {
    public Token create(Long idUser);
    public String extractSubject(String token);
}