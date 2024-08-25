package br.com.devtt.core.application.exceptions;

import java.io.Serial;

public class UserAlreadyExistsException extends CoreException {
    @Serial private static final long serialVersionUID = 1187143066666992900L;

    public UserAlreadyExistsException(String message) {
        super(message, 400);
    }
}