package br.com.devTT.core.abstractions.adapters.gateway.database.repositories;

import br.com.devTT.infrastructure.adapters.gateway.database.entities.UserEntity;

import java.util.Optional;

public interface UserRepository {
    Optional<UserEntity> findByEmail(String email);
}