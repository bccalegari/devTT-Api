package br.com.devtt.core.domain.entities;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class Role {
    private Long idRole;
    private String name;
}