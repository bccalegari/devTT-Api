package br.com.devtt.core.user.abstractions.application.usecases;

public interface GetUserUseCase<I, O> {
    O execute(I input, Long loggedUserId, String loggedUserRole, Integer loggedUserCompanyId);
}
