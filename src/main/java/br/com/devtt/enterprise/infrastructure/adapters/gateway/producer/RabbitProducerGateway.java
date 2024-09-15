package br.com.devtt.enterprise.infrastructure.adapters.gateway.producer;

import br.com.devtt.enterprise.abstractions.infrastructure.adapters.gateway.MailGateway;
import br.com.devtt.enterprise.infrastructure.adapters.dto.requests.MailProducerDto;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
@Qualifier("RabbitProducerGateway")
@Slf4j
public class RabbitProducerGateway implements MailGateway<MailProducerDto> {
    private final AmqpTemplate amqpTemplate;

    @Override
    public void send(MailProducerDto dto) {
        var message = createMessage(dto.message());
        sendMessage(dto.exchange(), dto.routingKey(), message);
    }

    private Message createMessage(String messageBody) {
        var messageProperties = new MessageProperties();
        messageProperties.setContentType(MessageProperties.CONTENT_TYPE_TEXT_PLAIN);
        return new Message(messageBody.getBytes(), messageProperties);
    }

    private void sendMessage(String exchange, String routingKey, Message message) {
        log.info("Sending message {} with routing key {} to exchange {}", message.toString(), routingKey, exchange);
        amqpTemplate.send(exchange, routingKey, message);
    }
}