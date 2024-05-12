package br.com.devTT.core.application.exceptions;

import java.io.Serial;

public class InvalidPasswordException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = -6545125051788081639L;

    public InvalidPasswordException(String message) {
        super(message);
    }
}