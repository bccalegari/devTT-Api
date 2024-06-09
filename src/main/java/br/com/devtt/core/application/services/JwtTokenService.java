package br.com.devtt.core.application.services;

import br.com.devtt.core.abstractions.application.services.TokenService;
import br.com.devtt.core.abstractions.domain.valueobjects.Token;
import br.com.devtt.core.domain.valueobjects.JwtToken;
import br.com.devtt.infrastructure.configuration.environment.JwtEnvironmentConfig;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Slf4j
@Service
@Qualifier("JwtTokenService")
public class JwtTokenService implements TokenService {
    private static final String EMPTY_STRING = "";

    @Override
    public Token create(Long idUser) {
        Instant expirationTime;
        String token;

        try {
            expirationTime = Instant.now().plusMillis(JwtEnvironmentConfig.EXPIRATION_TIME);
            Algorithm algorithm = Algorithm.HMAC256(JwtEnvironmentConfig.SECRET_KEY);

            token = JWT.create()
                    .withIssuer("devTT")
                    .withIssuedAt(Instant.now())
                    .withSubject(idUser.toString())
                    .withExpiresAt(expirationTime)
                    .sign(algorithm);
        } catch (JWTCreationException e){
            log.error(e.getMessage(), e);
            throw e;
        }

        return new JwtToken(token);
    }

    @Override
    public String extractSubject(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(JwtEnvironmentConfig.SECRET_KEY);
            return JWT.require(algorithm)
                    .build()
                    .verify(token)
                    .getSubject();
        } catch (JWTVerificationException exception){
            return EMPTY_STRING;
        }
    }
}