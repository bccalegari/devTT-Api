package br.com.devtt.core.user.invitation.abstractions.application.usecases;

public interface CreateUserRegistrationInvitationUseCase<T> {
    T execute(Long idUser, String email, Long createdBy, Long idLoggedUser);
}