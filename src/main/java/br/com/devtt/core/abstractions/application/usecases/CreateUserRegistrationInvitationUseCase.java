package br.com.devtt.core.abstractions.application.usecases;

public interface CreateUserRegistrationInvitationUseCase {
    void create(Long idUser, String email, Long createdBy, Long idLoggedUser);
}