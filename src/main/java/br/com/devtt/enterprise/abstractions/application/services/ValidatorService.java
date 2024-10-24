package br.com.devtt.enterprise.abstractions.application.services;

public interface ValidatorService<T> {
    boolean validate(T input);
}