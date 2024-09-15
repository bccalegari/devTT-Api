package br.com.devtt.enterprise.abstractions.application.services;

public interface MailService<T> {
    void send(T t);
}