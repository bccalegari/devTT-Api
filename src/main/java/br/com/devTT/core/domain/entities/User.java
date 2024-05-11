package br.com.devTT.core.domain.entities;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class User {
    private Long idUser;
    private String email;
    private String password;
}