package br.com.devtt.core.user.infrastructure.adapters.dto.requests;

import br.com.devtt.enterprise.infrastructure.configuration.spring.validators.abstractions.ListContainsValue;
import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Builder
@Getter
public class UpdateUserInputDto {
    @NotNull(message = "O campo idUser é obrigatório")
    @Min(value = 1, message = "O campo idUser deve ser maior que 0")
    private final Long idUser;

    @NotEmpty(message = "O campo nome não pode ser vazio")
    private final String name;

    @NotEmpty(message = "O campo sobrenome não pode ser vazio")
    private final String lastName;

    @NotEmpty(message = "O campo telefone não pode ser vazio")
    @Size(min = 10, max = 11, message = "O campo telefone deve ter entre 10 e 11 caracteres")
    @Pattern(regexp = "^[0-9]*$", message = "O campo telefone deve conter apenas números")
    private final String phone;

    @NotEmpty(message = "O campo email não pode ser vazio")
    @Email(message = "O campo email deve ser um e-mail válido")
    private final String email;

    @NotEmpty(message = "O campo senha não pode ser vazio")
    @Size(min = 6, message = "O campo senha deve ter no mínimo 6 caracteres")
    private final String password;

    @NotEmpty(message = "O campo CPF não pode ser vazio")
    @Size(min = 11, max = 11, message = "O campo CPF deve ter 11 caracteres")
    @Pattern(regexp = "^[0-9]*$", message = "O campo CPF deve conter apenas números")
    private final String cpf;

    @Past(message = "A data de nascimento deve ser anterior a data atual")
    private final LocalDate birthDate;

    @NotEmpty(message = "O campo sexo não pode ser vazio")
    @ListContainsValue(message = "O campo sexo deve ser 'M' ou 'F'")
    private final String sex;

    @NotEmpty(message = "O campo rua não pode ser vazio")
    private final String street;

    @Min(value = 1, message = "O campo número deve ser maior que 0")
    private final Integer streetNumber;

    @NotEmpty(message = "O campo bairro não pode ser vazio")
    private final String district;
    private final String complement;

    @NotEmpty(message = "O campo CEP não pode ser vazio")
    @Size(min = 8, max = 8, message = "O campo CEP deve ter 8 caracteres")
    @Pattern(regexp = "^[0-9]*$", message = "O campo CEP deve conter apenas números")
    private final String cep;

    @Min(value = 1, message = "O campo idCity deve ser maior que 0")
    private final Long idCity;
}
