package br.com.devTT.core.abstractions.mappers;

public interface DomainMapper<D, E>{
    D toDomain(E entity);
    E toEntity(D domain);
}