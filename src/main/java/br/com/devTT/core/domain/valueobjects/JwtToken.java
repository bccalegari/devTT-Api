package br.com.devTT.core.domain.valueobjects;

import br.com.devTT.core.domain.valueobjects.interfaces.Token;
import br.com.devTT.core.domain.valueobjects.interfaces.TokenVerifier;
import br.com.devTT.infrastructure.configuration.environment.JwtEnvironmentConfig;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class JwtToken implements Token {
    private final String token;
    private final LocalDateTime expirationDate;
    private final long idUser;

    @Override
    public boolean isExpired() {
        return expirationDate.isBefore(LocalDateTime.now());
    }

    @Override
    public boolean isValid(TokenVerifier verifier) {
        return verifier.verify(JwtEnvironmentConfig.SECRET_KEY, token);
    }
}