package br.com.devtt.core.company.infrastructure.adapters.dto.responses;

import java.io.Serializable;

public record GetCompanyOutputDto(
        Integer id,
        String name,
        String cnpj
) implements Serializable {}