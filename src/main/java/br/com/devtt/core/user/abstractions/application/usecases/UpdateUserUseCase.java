package br.com.devtt.core.user.abstractions.application.usecases;

public interface UpdateUserUseCase<I> {
    void execute(I input, Long userToBeUpdatedId, Long idLoggedUser, String loggedUserRole, Integer loggedUserCompanyId);
}