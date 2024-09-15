package br.com.devtt.core.company.abstractions.application.usecases;

public interface CreateCompanyUseCase {
    void execute(String name, String cnpj, Long idLoggedUser);
}