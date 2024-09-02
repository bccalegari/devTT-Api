package br.com.devtt.core.abstractions.application.usecases;

public interface UpdateCompanyUseCase {
    void update(Integer id, String name, String cnpj, Long idLoggedUser);
}