package br.com.devtt.core.user.infrastructure.adapters.dto.responses;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.time.LocalDate;

public record GetUserOutputDto (
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
) implements Serializable {
    @JsonCreator
    public GetUserOutputDto (
            @JsonProperty("id") Long id,
            @JsonProperty("name") String name,
            @JsonProperty("lastName") String lastName,
            @JsonProperty("phone") Long phone,
            @JsonProperty("email") String email,
            @JsonProperty("cpf") String cpf,
            @JsonProperty("birthDate") LocalDate birthDate,
            @JsonProperty("sex") String sex,
            @JsonProperty("address") Address address,
            @JsonProperty("company") Company company,
            @JsonProperty("role") Role role,
            @JsonProperty("firstAccess") boolean firstAccess
    ) {
        this.id = id;
        this.name = name;
        this.lastName = lastName;
        this.phone = phone;
        this.email = email;
        this.cpf = cpf;
        this.birthDate = birthDate;
        this.sex = sex;
        this.address = address;
        this.company = company;
        this.role = role;
        this.firstAccess = firstAccess;
    }

    public record Address(
            String street,
            Integer streetNumber,
            String district,
            String complement,
            String cep,
            City city
    ) implements Serializable {
        @JsonCreator
        public Address(
                @JsonProperty("street") String street,
                @JsonProperty("streetNumber") Integer streetNumber,
                @JsonProperty("district") String district,
                @JsonProperty("complement") String complement,
                @JsonProperty("cep") String cep,
                @JsonProperty("city") City city
        ) {
            this.street = street;
            this.streetNumber = streetNumber;
            this.district = district;
            this.complement = complement;
            this.cep = cep;
            this.city = city;
        }
    }

    public record City(
            Integer id,
            String name,
            State state
    ) implements Serializable {
        @JsonCreator
        public City(
                @JsonProperty("id") Integer id,
                @JsonProperty("name") String name,
                @JsonProperty("state") State state
        ) {
            this.id = id;
            this.name = name;
            this.state = state;
        }
    }

    public record State(
            Integer id,
            String name
    ) implements Serializable {
        @JsonCreator
        public State(
                @JsonProperty("id") Integer id,
                @JsonProperty("name") String name
        ) {
            this.id = id;
            this.name = name;
        }
    }

    public record Role(
            Integer id,
            String name
    ) implements Serializable {
        @JsonCreator
        public Role(
                @JsonProperty("id") Integer id,
                @JsonProperty("name") String name
        ) {
            this.id = id;
            this.name = name;
        }
    }

    public record Company(
            Integer id,
            String name,
            String cnpj
    ) implements Serializable {
        @JsonCreator
        public Company(
                @JsonProperty("id") Integer id,
                @JsonProperty("name") String name,
                @JsonProperty("cnpj") String cnpj
        ) {
            this.id = id;
            this.name = name;
            this.cnpj = cnpj;
        }
    }
}