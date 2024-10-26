package br.com.devtt.core.user.infrastructure.adapters.dto.requests;

import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Builder
@Getter
public class UpdateUserInputDto {
    @Pattern(regexp = "^$|\\S+.*", message = "O campo nome não pode ser vazio")
    private final String name;

    @Pattern(regexp = "^$|\\S+.*", message = "O campo sobrenome não pode ser vazio")
    private final String lastName;

    @Size(min = 10, max = 11, message = "O campo telefone deve ter entre 10 e 11 caracteres")
    @Pattern(regexp = "^[0-9]*$", message = "O campo telefone deve conter apenas números e não pode ser vazio")
    private final String phone;

    @Pattern(regexp = "^$|\\S+.*", message = "O campo email não pode ser vazio")
    @Email(message = "O campo email deve ser um e-mail válido")
    private final String email;

    @Pattern(regexp = "^$|\\S+.*", message = "O campo senha não pode ser vazio")
    @Size(min = 6, message = "O campo senha deve ter no mínimo 6 caracteres")
    private final String password;

    @Size(min = 11, max = 11, message = "O campo CPF deve ter 11 caracteres")
    @Pattern(regexp = "^[0-9]*$", message = "O campo CPF deve conter apenas números e não pode ser vazio")
    private final String cpf;

    @Past(message = "A data de nascimento deve ser anterior a data atual")
    private final LocalDate birthDate;

    @Pattern(regexp = "^[MF]$", message = "O campo sexo deve ser 'M' ou 'F' e não pode ser vazio")
    private final String sex;

    @Pattern(regexp = "^$|\\S+.*", message = "O campo rua não pode ser vazio")
    private final String street;

    @Min(value = 1, message = "O campo número deve ser maior que 0")
    private final Integer streetNumber;

    @Pattern(regexp = "^$|\\S+.*", message = "O campo bairro não pode ser vazio")
    private final String district;

    private final String complement;

    @Size(min = 8, max = 8, message = "O campo CEP deve ter 8 caracteres")
    @Pattern(regexp = "^[0-9]*$", message = "O campo CEP deve conter apenas números e não pode ser vazio")
    private final String cep;

    @Min(value = 1, message = "O campo idCity deve ser maior que 0")
    private final Long idCity;
}