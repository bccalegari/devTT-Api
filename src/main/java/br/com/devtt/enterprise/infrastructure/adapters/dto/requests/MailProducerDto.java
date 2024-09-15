package br.com.devtt.enterprise.infrastructure.adapters.dto.requests;

public record MailProducerDto(
        String exchange,
        String routingKey,
        String message
) {}