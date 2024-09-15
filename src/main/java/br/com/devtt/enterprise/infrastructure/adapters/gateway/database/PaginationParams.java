package br.com.devtt.enterprise.infrastructure.adapters.gateway.database;

import lombok.EqualsAndHashCode;
import lombok.Getter;

@EqualsAndHashCode
public class PaginationParams {
    private final Integer page;

    @Getter
    private final Integer size;

    public PaginationParams(Integer page, Integer size) {
        if (page == null) {
            page = 0;
        } else if (page < 0) {
            page = 0;
        }
        this.page = page;
        if (size == null) {
            size = 10;
        } else if (size > 10) {
            size = 10;
        }
        this.size = size;
    }

    public Integer getPage() {
        return page * size;
    }

    public Integer getCurrentPage() {
        return page;
    }
}