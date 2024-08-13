package br.com.devtt.core.domain.entities;

import br.com.devtt.core.domain.valueobjects.Address;
import br.com.devtt.core.domain.valueobjects.Auditing;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@Builder
public class User {
    private Long id;
    private String name;
    private String lastName;
    private Long phone;
    private String email;
    private String password;
    private String cpf;
    private String cnpj;
    private LocalDate birthDate;
    private Sex sex;
    private Address address;
    private Auditing auditing;
    private Company company;
    private Role role;

    public String getFullName() {
        return name + " " + lastName;
    }
}