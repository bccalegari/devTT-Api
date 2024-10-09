package br.com.devtt.core.company.application.exceptions;

import br.com.devtt.enterprise.application.exceptions.CoreException;

import java.io.Serial;

public class DeleteStandardCompanyException extends CoreException {
    @Serial
    private static final long serialVersionUID = 8561257591591370884L;
    public DeleteStandardCompanyException(String message) {
        super(message, 400);
    }
}