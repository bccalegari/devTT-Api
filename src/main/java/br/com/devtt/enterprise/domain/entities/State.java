package br.com.devtt.enterprise.domain.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class State {
    private Long id;
    private String name;
    private String isoAlpha2;
}