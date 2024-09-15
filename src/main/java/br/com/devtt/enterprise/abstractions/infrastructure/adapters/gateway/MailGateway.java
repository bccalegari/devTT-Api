package br.com.devtt.enterprise.abstractions.infrastructure.adapters.gateway;

public interface MailGateway<T> {
    void send(T t);
}