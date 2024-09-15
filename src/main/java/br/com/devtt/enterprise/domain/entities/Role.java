package br.com.devtt.enterprise.domain.entities;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class Role {
    private Integer id;
    private String name;
}