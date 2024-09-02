package br.com.devtt.core.application.exceptions;

import java.io.Serial;

public class CompanyNotFoundException extends CoreException {
    @Serial
    private static final long serialVersionUID = 4657909164543997203L;

    public CompanyNotFoundException(String message) {
        super(message, 400);
    }
}