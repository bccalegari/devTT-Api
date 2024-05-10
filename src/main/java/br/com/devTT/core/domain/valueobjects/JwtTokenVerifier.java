package br.com.devTT.core.domain.valueobjects;

import br.com.devTT.core.domain.valueobjects.interfaces.TokenVerifier;
import br.com.devTT.infrastructure.configuration.environment.JwtEnvironmentConfig;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class JwtTokenVerifier implements TokenVerifier {
    @Override
    public boolean verify(String secretKey, String token) {
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