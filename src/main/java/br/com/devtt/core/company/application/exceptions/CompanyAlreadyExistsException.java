package br.com.devtt.core.company.application.exceptions;

import br.com.devtt.enterprise.application.exceptions.CoreException;

import java.io.Serial;

public class CompanyAlreadyExistsException extends CoreException {
    @Serial private static final long serialVersionUID = -7450338782565264515L;

    public CompanyAlreadyExistsException(String message) {
        super(message, 400);
    }
}