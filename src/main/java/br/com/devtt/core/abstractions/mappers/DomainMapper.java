package br.com.devtt.core.abstractions.mappers;

public interface DomainMapper<D, E>{
    D toDomain(E entity);
    E toEntity(D domain);
}