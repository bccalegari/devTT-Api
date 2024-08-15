package br.com.devtt.core.abstractions.adapters.gateway.database.repositories;

import br.com.devtt.infrastructure.adapters.gateway.database.entities.UserEntity;

import java.util.Optional;

public interface UserRepository<T> {
    Optional<T> findByEmail(String email);
    Optional<T> findByPhoneOrEmailOrCpf(Long phone, String email, String cpf);
    UserEntity save(T userEntity);
}