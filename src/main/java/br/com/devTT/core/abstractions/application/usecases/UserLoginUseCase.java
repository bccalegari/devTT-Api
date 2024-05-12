package br.com.devTT.core.abstractions.application.usecases;

import br.com.devTT.core.abstractions.domain.valueobjects.Token;

public interface UserLoginUseCase {
    Token execute(String email, String password);
}