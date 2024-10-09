package br.com.devtt.core.user.abstractions.infrastructure.adapters.gateway;

import java.util.Optional;

public interface UserRepository<T> {
    Optional<T> findById(Long id);
    Optional<T> findByEmail(String email);
    Optional<T> findByPhoneOrEmailOrCpf(Long phone, String email, String cpf);
    T save(T entity);
}