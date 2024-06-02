package br.com.devtt.core.domain.valueobjects;

import br.com.devtt.core.abstractions.domain.valueobjects.Token;

public record JwtToken(String token) implements Token {
    @Override
    public String getToken() {
        return token;
    }
}