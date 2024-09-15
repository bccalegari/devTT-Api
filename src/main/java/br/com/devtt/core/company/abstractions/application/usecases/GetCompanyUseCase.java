package br.com.devtt.core.company.abstractions.application.usecases;

public interface GetCompanyUseCase<T> {
    T execute(Integer id);
}