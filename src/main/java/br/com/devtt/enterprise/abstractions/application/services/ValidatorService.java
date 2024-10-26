package br.com.devtt.enterprise.abstractions.application.services;

public interface ValidatorService<T> {
    boolean execute(T input);
}