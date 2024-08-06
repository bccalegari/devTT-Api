package br.com.devtt.infrastructure.configuration.environment;

import br.com.devtt.infrastructure.configuration.environment.exceptions.JwtSecretNotFoundException;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = lombok.AccessLevel.PRIVATE)
public class JwtEnvironmentConfig {
    public static final String SECRET_KEY = getSecretKeyEnv();
    public static final long EXPIRATION_TIME = 86400000; // 1 day in milliseconds

    private static String getSecretKeyEnv() {
        var env = EnvironmentConfig.getInstance();
        var secretKey = env.get("JWT_SECRET");
        if (secretKey == null || secretKey.isEmpty()) {
            throw new JwtSecretNotFoundException("A variável de ambiente JWT_SECRET não foi encontrada.");
        }
        return secretKey;
    }
}
