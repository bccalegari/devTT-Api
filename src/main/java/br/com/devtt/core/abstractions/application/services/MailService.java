package br.com.devtt.core.abstractions.application.services;

public interface MailService<T> {
    void send(T t);
}