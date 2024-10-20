package br.com.devtt.core.user.application.exceptions;

import br.com.devtt.enterprise.application.exceptions.CoreException;

import java.io.Serial;

public class UserNotFoundException extends CoreException {
    @Serial
    private static final long serialVersionUID = -6510438538188547321L;

    public UserNotFoundException(String message) {
        super(message, 404);
    }
}