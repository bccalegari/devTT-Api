package br.com.devTT.core.abstractions.application.services;

import br.com.devTT.core.abstractions.domain.entities.Token;

public interface TokenService {
    public Token create(Long idUser);
    public boolean isValid(String token, String secretKey);
}