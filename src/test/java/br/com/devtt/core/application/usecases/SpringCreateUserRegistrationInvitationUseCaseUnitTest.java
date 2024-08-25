package br.com.devtt.core.application.usecases;

import br.com.devtt.core.abstractions.infrastructure.adapters.gateway.database.repositories.UserRegistrationInvitationRepository;
import br.com.devtt.core.application.mappers.UserRegistrationInvitationMapper;
import br.com.devtt.infrastructure.adapters.gateway.database.entities.UserEntity;
import br.com.devtt.infrastructure.adapters.gateway.database.entities.UserRegistrationInvitationEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class SpringCreateUserRegistrationInvitationUseCaseUnitTest {
    @InjectMocks private SpringCreateUserRegistrationInvitationUseCase useCase;
    @Mock private UserRegistrationInvitationRepository<UserRegistrationInvitationEntity> repository;
    @Mock private UserRegistrationInvitationMapper mapper;
    private UserRegistrationInvitationEntity userRegistrationInvitationEntity;

    @BeforeEach
    void setUp() {
        userRegistrationInvitationEntity = UserRegistrationInvitationEntity.builder()
                .id(1L)
                .user(
                        UserEntity.builder()
                                .id(1L)
                                .build()
                )
                .email("email")
                .createdBy(1L)
                .expirationDt(LocalDateTime.now().plusDays(7))
                .token("token")
                .build();
        when(repository.save(any())).thenReturn(userRegistrationInvitationEntity);
    }

    @Test
    void shouldCreateUserRegistrationInvitation() {
        var idUser = 1L;
        var email = "email";
        var createdBy = 1L;
        var idLoggedUser = 1L;

        when(repository.findByUserId(idUser)).thenReturn(Optional.empty());
        when(mapper.toEntity(any())).thenReturn(userRegistrationInvitationEntity);

        var userRegistrationInvitationEntity = useCase.create(idUser, email, createdBy, idLoggedUser);

        assertNotNull(userRegistrationInvitationEntity);
        assertEquals(1L, userRegistrationInvitationEntity.getUser().getId());
        assertEquals("email", userRegistrationInvitationEntity.getEmail());
        assertNotNull(userRegistrationInvitationEntity.getToken());
        assertEquals(1L, userRegistrationInvitationEntity.getCreatedBy());
        assertTrue(
                userRegistrationInvitationEntity.getExpirationDt()
                        .isAfter(LocalDateTime.now())
        );

        verify(repository).findByUserId(idUser);
        verify(repository).save(any());

        verifyNoMoreInteractions(repository, mapper);
    }

    @Test
    void shouldDisableUserRegistrationInvitation() {
        var idUser = 1L;
        var email = "email";
        var createdBy = 1L;
        var idLoggedUser = 1L;

        when(repository.findByUserId(idUser)).thenReturn(Optional.of(userRegistrationInvitationEntity));
        doNothing().when(repository).disableRegistrationInvitation(userRegistrationInvitationEntity.getId(), idLoggedUser);
        when(mapper.toEntity(any())).thenReturn(userRegistrationInvitationEntity);

        var userRegistrationInvitationEntity = useCase.create(idUser, email, createdBy, idLoggedUser);

        assertNotNull(userRegistrationInvitationEntity);
        assertEquals(1L, userRegistrationInvitationEntity.getUser().getId());
        assertEquals("email", userRegistrationInvitationEntity.getEmail());
        assertNotNull(userRegistrationInvitationEntity.getToken());
        assertEquals(1L, userRegistrationInvitationEntity.getCreatedBy());
        assertTrue(
                userRegistrationInvitationEntity.getExpirationDt()
                        .isAfter(LocalDateTime.now())
        );

        verify(repository).findByUserId(idUser);
        verify(repository).disableRegistrationInvitation(userRegistrationInvitationEntity.getId(), idLoggedUser);
        verify(repository).save(any());

        verifyNoMoreInteractions(repository, mapper);
    }
}