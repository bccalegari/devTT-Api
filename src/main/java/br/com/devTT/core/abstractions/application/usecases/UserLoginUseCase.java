package br.com.devTT.core.abstractions.application.usecases;

import br.com.devTT.core.abstractions.domain.entities.Token;

public interface UserLoginUseCase {
    Token execute(String email, String password);
}