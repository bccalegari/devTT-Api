package br.com.devtt.core.company.abstractions.application.usecases;

public interface GetAllCompaniesUseCase<T> {
    T execute(String name, String cnpj, Integer page, Integer size);
}