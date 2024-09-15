package br.com.devtt.core.auth.abstractions.application.usecases;

import br.com.devtt.core.auth.abstractions.domain.valueobjects.Token;

public interface UserLoginUseCase {
    Token execute(String email, String password);
}