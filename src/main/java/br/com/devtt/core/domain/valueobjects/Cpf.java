package br.com.devtt.core.domain.valueobjects;

import lombok.Getter;

@Getter
public class Cpf {
    private final String value;

    public Cpf(String value) {
        if (value == null || value.isBlank() || value.length() != 11) {
            throw new IllegalArgumentException("Invalid CPF");
        }
        this.value = value;
    }
}