package br.com.devtt.core.domain.valueobjects;

import lombok.Getter;

@Getter
public class Cep {
    private final String value;

    public Cep(String value) {
        if (value == null || value.isBlank() || value.length() != 8) {
            throw new IllegalArgumentException("Invalid CEP");
        }
        this.value = value;
    }
}