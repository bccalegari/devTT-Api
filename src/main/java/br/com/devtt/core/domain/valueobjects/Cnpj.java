package br.com.devtt.core.domain.valueobjects;

import lombok.Getter;

@Getter
public class Cnpj {
    private final String value;

    public Cnpj(String value) {
        if (value == null || value.isBlank() || value.length() != 14) {
            throw new IllegalArgumentException("CNPJ inválido");
        }
        this.value = value;
    }
}