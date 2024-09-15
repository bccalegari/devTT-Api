package br.com.devtt.core.auth.application.exceptions;

import br.com.devtt.enterprise.application.exceptions.CoreException;

import java.io.Serial;

public class InvalidPasswordException extends CoreException {
    @Serial
    private static final long serialVersionUID = -6545125051788081639L;

    public InvalidPasswordException(String message) {
        super(message, 400);
    }
}