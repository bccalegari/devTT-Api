package br.com.devtt.core.abstractions.infrastructure.adapters.gateway;

public interface MailGateway<T> {
    void send(T t);
}