package br.com.devtt.core.company.infrastructure.adapters.dto.responses;

public record GetCompanyOutputDto(
        Integer id,
        String name,
        String cnpj
) {}