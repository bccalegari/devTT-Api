package br.com.devtt.core.user.abstractions.application.usecases;

public interface CreateUserUseCase<I> {
    void execute(I input, Long idLoggedUser, String loggedUserName);
}