package br.com.devtt.core.user.domain.entities;

import br.com.devtt.core.company.domain.entities.Company;
import br.com.devtt.enterprise.domain.entities.Role;
import br.com.devtt.enterprise.domain.valueobjects.Address;
import br.com.devtt.enterprise.domain.valueobjects.Auditing;
import br.com.devtt.core.user.domain.valueobjects.Cpf;
import br.com.devtt.core.user.domain.valueobjects.Sex;
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