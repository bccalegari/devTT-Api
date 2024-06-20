package br.com.devtt.core.domain.entities;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class User {
    private Long idUser;
    private String name;
    private String lastName;
    private String email;
    private String password;
    private Role role;

    public String getFullName() {
        return name + " " + lastName;
    }
}