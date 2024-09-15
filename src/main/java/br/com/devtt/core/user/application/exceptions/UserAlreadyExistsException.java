package br.com.devtt.core.user.application.exceptions;

import br.com.devtt.enterprise.application.exceptions.CoreException;

import java.io.Serial;

public class UserAlreadyExistsException extends CoreException {
    @Serial private static final long serialVersionUID = 1187143066666992900L;

    public UserAlreadyExistsException(String message) {
        super(message, 400);
    }
}