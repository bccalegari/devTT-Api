package br.com.devtt.enterprise.infrastructure.adapters.gateway.database;

import br.com.devtt.enterprise.abstractions.infrastructure.adapters.gateway.Page;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@AllArgsConstructor
@Getter
public class PageImpl<T> implements Page<T> {
    private final int page;
    private final int size;
    private final int currentPage;
    private final long totalElements;
    private final long totalPages;
    private final List<T> content;
}