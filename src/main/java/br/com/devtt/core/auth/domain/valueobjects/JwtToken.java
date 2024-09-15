package br.com.devtt.core.auth.domain.valueobjects;

import br.com.devtt.core.auth.abstractions.domain.valueobjects.Token;

public record JwtToken(String token) implements Token {
    @Override
    public String getValue() {
        return token;
    }
}