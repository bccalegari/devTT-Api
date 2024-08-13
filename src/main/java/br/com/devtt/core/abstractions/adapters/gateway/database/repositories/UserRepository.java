package br.com.devtt.core.abstractions.adapters.gateway.database.repositories;

import java.util.Optional;

public interface UserRepository<T> {
    Optional<T> findByEmail(String email);
    T save(T userEntity);
}