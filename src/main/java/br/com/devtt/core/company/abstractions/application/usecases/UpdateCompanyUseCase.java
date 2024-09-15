package br.com.devtt.core.company.abstractions.application.usecases;

public interface UpdateCompanyUseCase {
    void execute(Integer id, String name, String cnpj, Long idLoggedUser);
}