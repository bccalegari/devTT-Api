package br.com.devtt.infrastructure.adapters.dto.requests;

public record MailProducerDto(
        String exchange,
        String routingKey,
        String message
) {}