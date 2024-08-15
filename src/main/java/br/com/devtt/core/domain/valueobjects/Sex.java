package br.com.devtt.core.domain.valueobjects;

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
            if (sex.getCode().equals(code.toString().toUpperCase().charAt(0))) {
                return sex;
            }
        }
        throw new IllegalArgumentException("Invalid code: " + code);
    }

    public static Sex fromCode(String code) {
        for (Sex sex : Sex.values()) {
            if (sex.getCode().equals(code.toUpperCase().charAt(0))) {
                return sex;
            }
        }
        throw new IllegalArgumentException("Invalid code: " + code);
    }
}