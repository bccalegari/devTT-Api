package br.com.devtt.core.abstractions.infrastructure.adapters.gateway.database.repositories;

import java.util.Optional;

public interface CompanyRepository<T> {
    Optional<T> findById(Integer id);
    Optional<T> findByCnpj(String cnpj);
    T save(T entity);
    void update(T entity);
}