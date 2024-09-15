package br.com.devtt.core.company.abstractions.infrastructure.adapters.gateway;

import br.com.devtt.enterprise.abstractions.infrastructure.adapters.gateway.Page;
import br.com.devtt.enterprise.infrastructure.adapters.gateway.database.PaginationParams;

import java.util.List;
import java.util.Optional;

public interface CompanyRepository<T> {
    Optional<T> findById(Integer id);
    Optional<T> findByCnpj(String cnpj);
    Page<T> findAll(PaginationParams paginationParams);
    Page<T> findAllByNameOrCnpj(String name, String cnpj, PaginationParams paginationParams);
    List<String> getAllNames();
    List<String> getAllCnpjs();
    T save(T entity);
    void update(T entity);
    void delete(T entity);
}