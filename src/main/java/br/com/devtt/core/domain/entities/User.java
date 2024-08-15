package br.com.devtt.core.domain.entities;

import br.com.devtt.core.domain.valueobjects.Address;
import br.com.devtt.core.domain.valueobjects.Auditing;
import br.com.devtt.core.domain.valueobjects.Cpf;
import br.com.devtt.core.domain.valueobjects.Sex;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Builder
public class User {
    private Long id;
    private String name;
    private String lastName;
    private Long phone;
    private String email;
    @Setter
    private String password;
    private Cpf cpf;
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