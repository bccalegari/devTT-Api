package br.com.devTT.core.domain.valueobjects;

import br.com.devTT.core.domain.valueobjects.interfaces.Token;
import br.com.devTT.infrastructure.configuration.environment.JwtEnvironmentConfig;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class JwtToken implements Token {
    private final String token;
    private final LocalDateTime expirationDate;
    private final long idUser;

    public boolean isExpired() {
        return expirationDate.isBefore(LocalDateTime.now());
    }

    public boolean isValid() {
        try {
            Algorithm algorithm = Algorithm.HMAC256(JwtEnvironmentConfig.SECRET_KEY);
            JWTVerifier verifier = JWT.require(algorithm)
                    .withIssuer("devTT")
                    .build();

            verifier.verify(token);
        } catch (JWTVerificationException exception){
            return false;
        }

        return true;
    }
}