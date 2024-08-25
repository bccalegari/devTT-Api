package br.com.devtt.core.abstractions.application.usecases;

public interface CreateUserUseCase<I> {
    void create(I input, Long idLoggedUser, String loggedUserName);
}