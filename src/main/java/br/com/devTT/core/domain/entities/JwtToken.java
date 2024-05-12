package br.com.devTT.core.domain.entities;

import br.com.devTT.core.abstractions.domain.entities.Token;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class JwtToken implements Token {
    private final String token;
    private final LocalDateTime expirationDate;
    private final Long idUser;

    @Override
    public boolean isExpired() {
        return expirationDate.isBefore(LocalDateTime.now());
    }
}