package br.com.devtt.core.company.infrastructure.adapters.dto.responses;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

public record GetCompanyOutputDto(
        Integer id,
        String name,
        String cnpj
) implements Serializable {
    @JsonCreator
    public GetCompanyOutputDto(
            @JsonProperty("id") Integer id,
            @JsonProperty("name") String name,
            @JsonProperty("cnpj") String cnpj
    ) {
        this.id = id;
        this.name = name;
        this.cnpj = cnpj;
    }
}