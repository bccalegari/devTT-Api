package br.com.devtt.core.company.infrastructure.adapters.gateway.cache;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum CompanyCacheKeys {
    COMPANY("company::id=%s"),
    COMPANIES_PATTERN("companies"),
    COMPANIES_PAGED("companies::name=%s&cnpj=%s&page=%s&size=%s");

    private final String key;
}