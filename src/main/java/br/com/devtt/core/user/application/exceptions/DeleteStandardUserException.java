package br.com.devtt.core.user.application.exceptions;

import br.com.devtt.enterprise.application.exceptions.CoreException;

import java.io.Serial;

public class DeleteStandardUserException extends CoreException {
    @Serial
    private static final long serialVersionUID = 7794192886740204303L;

    public DeleteStandardUserException(String message) {
        super(message, 400);
    }
}