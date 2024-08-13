package br.com.devtt.core.abstractions.mappers;

public interface AdapterMapper<D, DT>{
    D toDomain(DT dto);
    DT toDto(D domain);
}