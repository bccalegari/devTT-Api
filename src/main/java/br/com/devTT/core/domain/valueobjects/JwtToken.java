package br.com.devTT.core.domain.valueobjects;

import br.com.devTT.core.abstractions.domain.valueobjects.Token;

public record JwtToken(String token) implements Token {
    @Override
    public String getToken() {
        return token;
    }
}