package br.com.devTT.core.application.services;

import br.com.devTT.core.abstractions.application.services.TokenService;
import br.com.devTT.core.abstractions.domain.entities.Token;
import br.com.devTT.core.domain.entities.JwtToken;
import br.com.devTT.infrastructure.configuration.environment.JwtEnvironmentConfig;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Slf4j
@Service
@Qualifier("JwtTokenService")
public class JwtTokenService implements TokenService {
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
        } catch (JWTCreationException exception){
            log.error("Error creating JWT token", exception);
            throw exception;
        }

        return new JwtToken(token, LocalDateTime.ofInstant(expirationTime, ZoneOffset.UTC), idUser);
    }

    @Override
    public boolean isValid(String token, String secretKey) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(JwtEnvironmentConfig.SECRET_KEY);
            JWTVerifier verifier = JWT.require(algorithm)
                    .build();

            verifier.verify(token);
        } catch (JWTVerificationException exception){
            return false;
        }

        return true;
    }
}