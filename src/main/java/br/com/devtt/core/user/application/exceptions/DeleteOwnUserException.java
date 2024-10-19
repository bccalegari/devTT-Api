package br.com.devtt.core.user.application.exceptions;

import br.com.devtt.enterprise.application.exceptions.CoreException;

import java.io.Serial;

public class DeleteOwnUserException extends CoreException {
    @Serial
    private static final long serialVersionUID = 1572416996941502410L;

    public DeleteOwnUserException(String message) {
        super(message, 400);
    }
}