package br.com.devtt.enterprise.abstractions.application.services;

public interface ComparatorService {
    <T> boolean hasChanges(T newValue, T oldValue);
}