package br.com.devtt.infrastructure.adapters.dto.responses;

import lombok.Getter;

@Getter
public class AuthLoginOutputDto extends OutputDto {
    private final String bearerToken;

    public AuthLoginOutputDto(String message, String bearerToken) {
        super(message);
        this.bearerToken = bearerToken;
    }
}