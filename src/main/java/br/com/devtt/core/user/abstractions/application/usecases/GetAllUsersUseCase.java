package br.com.devtt.core.user.abstractions.application.usecases;

public interface GetAllUsersUseCase<O> {
    O execute(Integer page, Integer size);
}
