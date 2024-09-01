package br.com.devtt.core.abstractions.infrastructure.adapters.gateway.database.repositories;

import java.util.Optional;

public interface CompanyRepository<T> {
    Optional<T> findByCnpj(String cnpj);
    T save(T entity);
}