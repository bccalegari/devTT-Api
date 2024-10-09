package br.com.devtt.core.company.application.exceptions;

import br.com.devtt.enterprise.application.exceptions.CoreException;

import java.io.Serial;

public class DeleteOwnCompanyException extends CoreException {
    @Serial
    private static final long serialVersionUID = -6540344701237683804L;
    public DeleteOwnCompanyException(String message) {
        super(message, 400);
    }
}