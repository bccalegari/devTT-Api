package br.com.devtt.core.application.services;

import br.com.devtt.core.abstractions.infrastructure.adapters.gateway.MailGateway;
import br.com.devtt.core.domain.valueobjects.UserInvitationEmailPayload;
import br.com.devtt.infrastructure.adapters.dto.requests.MailProducerDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class RabbitMailProducerServiceUnitTest {
    @InjectMocks private RabbitMailProducerService rabbitMailProducerService;
    @Mock private MailGateway<MailProducerDto> mailGateway;
    private UserInvitationEmailPayload userInvitationEmailPayload;
    private MailProducerDto mailProducerDto;

    @BeforeEach
    public void setUp() {
        userInvitationEmailPayload = UserInvitationEmailPayload.builder()
                .fullName("Full name")
                .email("email@email.com")
                .token("token")
                .creatorName("Creator name")
                .build();
        var message = "{\"fullName\":\"Full name\",\"email\":\"email@email.com\",\"token\":\"token\",\"creatorName\":\"Creator name\"}";
        mailProducerDto = new MailProducerDto("mail-exchange", "mail", message);
    }

    @Test
    public void shouldProduceMail() {
        doNothing()
                .when(mailGateway)
                .send(mailProducerDto);
        rabbitMailProducerService.send(userInvitationEmailPayload);
        verify(mailGateway).send(mailProducerDto);
    }

    @Test
    public void shouldThrowRuntimeExceptionWhenErrorParsingPayload() {
        var invalidPayload = UserInvitationEmailPayload.builder()
                .fullName("Full name")
                .email("email")
                .token("token")
                .creatorName("")
                .build();
        var message = "{\"fullName\":\"Full name\",\"email\":\"email@email.com\",\"token\":\"token\",\"creatorName\":}";
        mailProducerDto = new MailProducerDto("mail-exchange", "mail", message);
        doNothing()
                .when(mailGateway)
                .send(mailProducerDto);
        assertThrows(RuntimeException.class, () -> rabbitMailProducerService.send(invalidPayload));
    }
}