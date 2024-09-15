package br.com.devtt.enterprise.abstractions.infrastructure.adapters.mappers;

public interface AdapterMapper<D, DT>{
    D toDomain(DT dto);
    DT toDto(D domain);
}