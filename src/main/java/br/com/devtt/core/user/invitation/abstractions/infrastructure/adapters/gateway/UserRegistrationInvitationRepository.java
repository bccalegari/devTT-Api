package br.com.devtt.core.user.invitation.abstractions.infrastructure.adapters.gateway;

import java.util.Optional;

public interface UserRegistrationInvitationRepository<T> {
    Optional<T> findByUserId(Long userId);
    void disableRegistrationInvitation(Long idUserRegistrationInvitation, Long idLoggedUser);
    void disableAllRegistrationInvitationsByUserId(Long userId, Long idLoggedUser);
    void disableAllRegistrationInvitationsByCompanyId(Integer companyId, Long idLoggedUser);
    T save(T entity);
}