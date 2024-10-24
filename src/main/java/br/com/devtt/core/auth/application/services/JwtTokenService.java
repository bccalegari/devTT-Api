package br.com.devtt.core.auth.application.services;

import br.com.devtt.core.auth.abstractions.application.services.TokenService;
import br.com.devtt.core.auth.abstractions.domain.valueobjects.Token;
import br.com.devtt.core.auth.application.exceptions.JwtSecretNotFoundException;
import br.com.devtt.core.auth.domain.valueobjects.JwtToken;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Slf4j
@Service
@AllArgsConstructor
@Qualifier("JwtTokenService")
public class JwtTokenService implements TokenService {
    private static final Long EXPIRATION_TIME = 86400000L; // 1 day in milliseconds
    private static final String EMPTY_STRING = "";
    private final Environment env;

    @Override
    public Token create(Long idUser, String name, String role, Integer idCompany) {
        Instant expirationTime;
        String token;

        try {
            expirationTime = Instant.now().plusMillis(EXPIRATION_TIME);
            var algorithm = Algorithm.HMAC256(getJwtSecretKey());

            token = JWT.create()
                    .withIssuer("devTT")
                    .withIssuedAt(Instant.now())
                    .withSubject(idUser.toString())
                    .withClaim("name", name)
                    .withClaim("role", role)
                    .withClaim("idCompany", idCompany)
                    .withExpiresAt(expirationTime)
                    .sign(algorithm);
        } catch (JWTCreationException | JwtSecretNotFoundException e){
            log.error(e.getMessage(), e);
            throw e;
        }

        return new JwtToken(token);
    }

    @Override
    public String extractSubject(String token) {
        try {
            var algorithm = Algorithm.HMAC256(getJwtSecretKey());
            return JWT.require(algorithm)
                    .build()
                    .verify(token)
                    .getSubject();
        } catch (Exception e){
            return EMPTY_STRING;
        }
    }

    @Override
    public String extractName(String token) {
        try {
            var algorithm = Algorithm.HMAC256(getJwtSecretKey());
            return JWT.require(algorithm)
                    .build()
                    .verify(token)
                    .getClaim("name")
                    .asString();
        } catch (Exception e){
            return EMPTY_STRING;
        }
    }

    @Override
    public String extractRole(String token) {
        try {
            var algorithm = Algorithm.HMAC256(getJwtSecretKey());
            return JWT.require(algorithm)
                    .build()
                    .verify(token)
                    .getClaim("role")
                    .asString();
        } catch (Exception e){
            return EMPTY_STRING;
        }
    }

    @Override
    public Integer extractCompanyId(String token) {
        try {
            var algorithm = Algorithm.HMAC256(getJwtSecretKey());
            return JWT.require(algorithm)
                    .build()
                    .verify(token)
                    .getClaim("idCompany")
                    .asInt();
        } catch (Exception e){
            return 0;
        }
    }

    private String getJwtSecretKey() {
        var secretKey = env.getProperty("JWT_SECRET");
        if (secretKey == null || secretKey.isEmpty()) {
            throw new JwtSecretNotFoundException();
        }
        return secretKey;
    }
}