package br.com.devtt.enterprise.application.services;

import br.com.devtt.core.user.invitation.application.services.RabbitInvitationMailProducerService;
import br.com.devtt.enterprise.abstractions.infrastructure.adapters.gateway.MailGateway;
import br.com.devtt.core.user.invitation.infrastructure.adapters.dto.UserInvitationEmailPayload;
import br.com.devtt.enterprise.infrastructure.adapters.dto.requests.MailProducerDto;
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
public class RabbitInvitationMailProducerServiceUnitTest {
    @InjectMocks private RabbitInvitationMailProducerService rabbitInvitationMailProducerService;
    @Mock private MailGateway<MailProducerDto> mailGateway;
    private UserInvitationEmailPayload userInvitationEmailPayload;
    private MailProducerDto mailProducerDto;

    @BeforeEach
    void setUp() {
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
    void shouldProduceMail() {
        doNothing()
                .when(mailGateway)
                .send(mailProducerDto);
        rabbitInvitationMailProducerService.send(userInvitationEmailPayload);
        verify(mailGateway).send(mailProducerDto);
    }

    @Test
    void shouldThrowRuntimeExceptionWhenErrorParsingPayload() {
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
        assertThrows(RuntimeException.class, () -> rabbitInvitationMailProducerService.send(invalidPayload));
    }
}