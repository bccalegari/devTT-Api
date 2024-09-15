package br.com.devtt.core.company.abstractions.application.usecases;

public interface DeleteCompanyUseCase {
    void execute(Integer id, Long idLoggedUser);
}