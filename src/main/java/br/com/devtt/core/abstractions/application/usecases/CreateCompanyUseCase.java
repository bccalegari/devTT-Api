package br.com.devtt.core.abstractions.application.usecases;

public interface CreateCompanyUseCase {
    void create(String name, String cnpj, Long idLoggedUser);
}