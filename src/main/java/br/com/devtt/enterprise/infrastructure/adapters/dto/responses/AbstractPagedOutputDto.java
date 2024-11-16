package br.com.devtt.enterprise.infrastructure.adapters.dto.responses;

import lombok.Getter;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@Getter
public abstract class AbstractPagedOutputDto {
    protected final Integer currentPage;
    protected final Integer size;
    protected final Long totalElements;
    protected final Long totalPages;
}