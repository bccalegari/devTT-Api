package br.com.devtt.core.abstractions.application.usecases;

import br.com.devtt.core.abstractions.domain.valueobjects.Token;

public interface UserLoginUseCase {
    Token execute(String email, String password);
}