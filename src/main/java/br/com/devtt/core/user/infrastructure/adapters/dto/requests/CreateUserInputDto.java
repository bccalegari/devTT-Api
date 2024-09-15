package br.com.devtt.core.user.infrastructure.adapters.dto.requests;

import br.com.devtt.enterprise.infrastructure.configuration.spring.validators.abstractions.ListContainsValue;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@AllArgsConstructor
@Builder
@Getter
public class CreateUserInputDto {
    @NotNull(message = "O campo nome é obrigatório")
    @NotEmpty(message = "O campo nome não pode ser vazio")
    private final String name;

    @NotNull(message = "O campo sobrenome é obrigatório")
    @NotEmpty(message = "O campo sobrenome não pode ser vazio")
    private final String lastName;

    @NotNull(message = "O campo telefone é obrigatório")
    @NotEmpty(message = "O campo telefone não pode ser vazio")
    @Size(min = 10, max = 11, message = "O campo telefone deve ter entre 10 e 11 caracteres")
    private final String phone;

    @NotNull(message = "O campo email é obrigatório")
    @NotEmpty(message = "O campo email não pode ser vazio")
    private final String email;

    @NotNull(message = "O campo senha é obrigatório")
    @NotEmpty(message = "O campo senha não pode ser vazio")
    @Size(min = 6, message = "O campo senha deve ter no mínimo 6 caracteres")
    private final String password;

    @NotNull(message = "O campo CPF é obrigatório")
    @NotEmpty(message = "O campo CPF não pode ser vazio")
    @Size(min = 11, max = 11, message = "O campo CPF deve ter 11 caracteres")
    private final String cpf;

    @NotNull(message = "O campo data de nascimento é obrigatório")
    @Past(message = "A data de nascimento deve ser anterior a data atual")
    private final LocalDate birthDate;

    @NotNull(message = "O campo sexo é obrigatório")
    @NotEmpty(message = "O campo sexo não pode ser vazio")
    @ListContainsValue(message = "O campo sexo deve ser 'M' ou 'F'")
    private final String sex;

    @NotNull(message = "O campo rua é obrigatório")
    @NotEmpty(message = "O campo rua não pode ser vazio")
    private final String street;

    @NotNull(message = "O campo número é obrigatório")
    @Min(value = 1, message = "O campo número deve ser maior que 0")
    private final Integer streetNumber;

    @NotNull(message = "O campo bairro é obrigatório")
    @NotEmpty(message = "O campo bairro não pode ser vazio")
    private final String district;
    private final String complement;

    @NotNull(message = "O campo CEP é obrigatório")
    @NotEmpty(message = "O campo CEP não pode ser vazio")
    @Size(min = 8, max = 8, message = "O campo CEP deve ter 8 caracteres")
    private final String cep;

    @NotNull(message = "O campo idCity é obrigatório")
    private final Long idCity;

    @NotNull(message = "O campo idRole é obrigatório")
    private final Integer idRole;

    @NotNull(message = "O campo idCompany é obrigatório")
    private final Integer idCompany;
}