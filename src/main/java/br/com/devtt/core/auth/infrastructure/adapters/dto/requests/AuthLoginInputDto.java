package br.com.devtt.core.auth.infrastructure.adapters.dto.requests;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AuthLoginInputDto {
    @NotNull(message = "O campo email é obrigatório")
    @NotEmpty(message = "O campo email não pode ser vazio")
    private String email;

    @NotNull(message = "O campo senha é obrigatório")
    @NotEmpty(message = "O campo senha não pode ser vazio")
    private String password;
}