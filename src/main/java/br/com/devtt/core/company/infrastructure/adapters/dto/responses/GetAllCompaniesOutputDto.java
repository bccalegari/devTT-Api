package br.com.devtt.core.company.infrastructure.adapters.dto.responses;

import java.util.List;

public record GetAllCompaniesOutputDto(
        Integer currentPage,
        Integer size,
        Long totalElements,
        Long totalPages,
        List<GetCompanyOutputDto> companies
) {}