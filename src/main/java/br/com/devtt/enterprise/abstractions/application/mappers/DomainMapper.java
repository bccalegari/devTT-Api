package br.com.devtt.enterprise.abstractions.application.mappers;

public interface DomainMapper<D, E>{
    D toDomain(E entity);
    E toEntity(D domain);
}