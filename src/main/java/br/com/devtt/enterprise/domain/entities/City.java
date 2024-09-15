package br.com.devtt.enterprise.domain.entities;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class City {
    private Long id;
    private String name;
    private String stateAcronym;
    private final State state;
}