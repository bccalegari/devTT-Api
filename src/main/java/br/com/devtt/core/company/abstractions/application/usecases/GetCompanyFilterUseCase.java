package br.com.devtt.core.company.abstractions.application.usecases;

public interface GetCompanyFilterUseCase<T> {
    T execute(String filter);
}