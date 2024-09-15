package br.com.devtt.core.auth.infrastructure.adapters.dto.responses;

import br.com.devtt.enterprise.infrastructure.adapters.dto.responses.OutputDto;
import lombok.Getter;

@Getter
public class AuthLoginOutputDto extends OutputDto {
    private final String bearerToken;

    public AuthLoginOutputDto(String message, String bearerToken) {
        super(message);
        this.bearerToken = bearerToken;
    }
}