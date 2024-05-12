package br.com.devTT.infrastructure.configuration.environment;

import br.com.devTT.infrastructure.configuration.environment.exceptions.JwtSecretNotFoundException;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = lombok.AccessLevel.PRIVATE)
public class JwtEnvironmentConfig {
    public static final String SECRET_KEY = getSecretKeyEnv();
    public static final long EXPIRATION_TIME = 86400000; // 1 day in milliseconds

    private static String getSecretKeyEnv() {
        String secretKey = System.getenv("JWT_SECRET");
        if (secretKey == null || secretKey.isEmpty()) {
            throw new JwtSecretNotFoundException("JWT_SECRET not found in environment variables");
        }
        return secretKey;
    }
}
