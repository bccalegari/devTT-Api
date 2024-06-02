package br.com.devtt.core.abstractions.adapters.gateway.database.repositories;

import br.com.devtt.infrastructure.adapters.gateway.database.entities.UserEntity;

import java.util.Optional;

public interface UserRepository {
    Optional<UserEntity> findByEmail(String email);
}