package br.com.devtt.core.domain.entities;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class Company {
    private Long id;
    private String name;
}