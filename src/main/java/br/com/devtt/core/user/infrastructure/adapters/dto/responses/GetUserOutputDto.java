package br.com.devtt.core.user.infrastructure.adapters.dto.responses;

import java.time.LocalDate;

public record GetUserOutputDto(
    Long id,
    String name,
    String lastName,
    Long phone,
    String email,
    String cpf,
    LocalDate birthDate,
    String sex,
    Address address,
    Company company,
    Role role,
    boolean firstAccess
) {
    public record Address(
            String street,
            Integer streetNumber,
            String district,
            String complement,
            String cep,
            City city
    ) {}

    public record City(
            Integer id,
            String name,
            State state
    ) {}

    public record State(
            Integer id,
            String name
    ) {}

    public record Role(
            Integer id,
            String name
    ) {}

    public record Company(
            Integer id,
            String name,
            String cnpj
    ) {}
}