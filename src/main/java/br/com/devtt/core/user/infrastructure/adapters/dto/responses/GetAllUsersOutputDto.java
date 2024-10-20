package br.com.devtt.core.user.infrastructure.adapters.dto.responses;

import java.util.List;

public record GetAllUsersOutputDto(
        Integer currentPage,
        Integer size,
        Long totalElements,
        Long totalPages,
        List<GetUserOutputDto> users
) {}