package br.com.devtt.core.domain.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Sex {
    MALE ('M', "Masculino"),
    FEMALE('F', "Feminino");

    private final Character code;
    private final String description;

    public static Sex fromCode(Character code) {
        for (Sex sex : Sex.values()) {
            if (sex.getCode().equals(code)) {
                return sex;
            }
        }
        throw new IllegalArgumentException("Invalid code: " + code);
    }
}