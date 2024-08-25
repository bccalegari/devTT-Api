package br.com.devtt.core.application.services;

import br.com.devtt.core.abstractions.application.services.MailService;
import br.com.devtt.core.abstractions.infrastructure.adapters.gateway.MailGateway;
import br.com.devtt.core.domain.valueobjects.UserInvitationEmailPayload;
import br.com.devtt.infrastructure.adapters.dto.requests.MailProducerDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
@Qualifier("RabbitMailProducerService")
@Slf4j
public class RabbitMailProducerService implements MailService<UserInvitationEmailPayload> {
    private static final String EXCHANGE = "mail-exchange";
    private static final String ROUTING_KEY = "mail";
    private final MailGateway<MailProducerDto> mailGateway;

    @Autowired
    public RabbitMailProducerService(
            @Qualifier("RabbitProducerGateway") MailGateway<MailProducerDto> mailGateway
    ) {
        this.mailGateway = mailGateway;
    }

    @Override
    public void send(UserInvitationEmailPayload payload) {
        try {
            var message = parsePayload(payload);
            var dto = new MailProducerDto(EXCHANGE, ROUTING_KEY, message);
            mailGateway.send(dto);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error parsing payload", e);
        }
    }

    private String parsePayload(UserInvitationEmailPayload payload) throws JsonProcessingException {
        var objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(payload);
    }
}