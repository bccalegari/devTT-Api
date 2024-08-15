package br.com.devtt.core.abstractions.adapters.gateway.database.repositories;

import java.util.Optional;

public interface UserRegistrationInvitationRepository<T> {
    Optional<T> findByUserId(Long userId);
    void disableRegistrationInvitation(Long idUserRegistrationInvitation, Long idLoggedUser);
    T save(T entity);
}