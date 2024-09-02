package br.com.devtt.core.abstractions.application.services;

public interface ComparatorService {
    <T> boolean hasChanges(T newValue, T oldValue);
}