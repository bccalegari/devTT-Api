package br.com.devtt.core.user.abstractions.application.usecases;

public interface DeleteUserUseCase<I> {
    void execute(I input, Long idLoggedUser);
}