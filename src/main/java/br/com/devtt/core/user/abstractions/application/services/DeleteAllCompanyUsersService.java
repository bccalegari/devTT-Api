package br.com.devtt.core.user.abstractions.application.services;

public interface DeleteAllCompanyUsersService<I> {
    void execute(I input, Long idLoggedUser);
}
