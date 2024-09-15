package br.com.devtt.core.company.infrastructure.adapters.dto.responses;

import java.util.List;

public record GetCompanyFilterOutputDto(
        List<String> data
) {}