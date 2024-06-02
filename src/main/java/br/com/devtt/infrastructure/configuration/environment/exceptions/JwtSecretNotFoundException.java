package br.com.devtt.infrastructure.configuration.environment.exceptions;

import java.io.Serial;

public class JwtSecretNotFoundException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = 9069964177833946801L;

    public JwtSecretNotFoundException(String message) {
        super(message);
    }
}
