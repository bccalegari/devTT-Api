package br.com.devTT.core.application.exceptions;

import java.io.Serial;

public class UserNotFoundException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = -6510438538188547321L;

    public UserNotFoundException(String message) {
        super(message);
    }
}