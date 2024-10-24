package br.com.devtt.core.user.abstractions.infrastructure.adapters.gateway;

import br.com.devtt.enterprise.abstractions.infrastructure.adapters.gateway.Page;
import br.com.devtt.enterprise.infrastructure.adapters.gateway.database.PaginationParams;

import java.util.Optional;

public interface UserRepository<T> {
    Optional<T> findById(Long id);
    Optional<T> findByPhone(Long phone);
    Optional<T> findByEmail(String email);
    Optional<T> findByCpf(String cpf);
    Optional<T> findByPhoneOrEmailOrCpf(Long phone, String email, String cpf);
    Page<T> findAll(PaginationParams paginationParams, String search, Integer idCompany);
    T save(T entity);
    void delete(T entity);
    void deleteByCompanyId(Integer idCompany, Long idLoggedUser);
}