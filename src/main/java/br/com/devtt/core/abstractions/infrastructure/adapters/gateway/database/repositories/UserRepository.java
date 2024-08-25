package br.com.devtt.core.abstractions.infrastructure.adapters.gateway.database.repositories;

import java.util.Optional;

public interface UserRepository<T> {
    Optional<T> findByEmail(String email);
    Optional<T> findByPhoneOrEmailOrCpf(Long phone, String email, String cpf);
    T save(T entity);
}