package br.com.devtt.enterprise.domain.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor
@Getter
@Builder
public class State {
    private Long id;
    private String name;
    private String isoAlpha2;
}