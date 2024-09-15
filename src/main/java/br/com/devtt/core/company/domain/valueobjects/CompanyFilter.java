package br.com.devtt.core.company.domain.valueobjects;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum CompanyFilter {
    NAME("name"),
    CNPJ("cnpj");

    private final String value;

    public static CompanyFilter fromString(String value) {
        for (CompanyFilter companyFilter : CompanyFilter.values()) {
            if (companyFilter.value.equalsIgnoreCase(value)) {
                return companyFilter;
            }
        }
        return null;
    }
}