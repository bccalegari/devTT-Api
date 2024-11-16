package br.com.devtt.core.user.application.usecases;

import br.com.devtt.core.user.abstractions.infrastructure.adapters.gateway.UserRepository;
import br.com.devtt.core.user.application.exceptions.DeleteOwnUserException;
import br.com.devtt.core.user.application.exceptions.DeleteStandardUserException;
import br.com.devtt.core.user.application.exceptions.UserNotFoundException;
import br.com.devtt.core.user.infrastructure.adapters.gateway.cache.UserCacheKeys;
import br.com.devtt.core.user.infrastructure.adapters.gateway.database.entities.UserEntity;
import br.com.devtt.core.user.invitation.abstractions.infrastructure.adapters.gateway.UserRegistrationInvitationRepository;
import br.com.devtt.core.user.invitation.infrastructure.adapters.gateway.database.entities.UserRegistrationInvitationEntity;
import br.com.devtt.enterprise.abstractions.infrastructure.adapters.gateway.CacheGateway;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class SpringDeleteUserUseCaseUnitTest {
    @InjectMocks private SpringDeleteUserUseCase springDeleteUserUseCase;
    @Mock private UserRepository<UserEntity> userRepository;
    @Mock private UserRegistrationInvitationRepository<UserRegistrationInvitationEntity> userRegistrationInvitationRepository;
    @Mock private CacheGateway cacheGateway;

    @Test
    void shouldThrowUserNotFoundExceptionWhenUserDoesNotExist() {
        var idUser = 2L;
        var idLoggedUser = 2L;

        when(userRepository.findById(idUser)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> springDeleteUserUseCase.execute(idUser, idLoggedUser));
        verify(userRepository).findById(idUser);
        verifyNoInteractions(userRegistrationInvitationRepository, cacheGateway);
    }

    @Test
    void shouldThrowDeleteOwnUserExceptionWhenUserIsLoggedUser() {
        var idUser = 2L;
        var idLoggedUser = 2L;
        var userEntity = UserEntity.builder().id(idUser).build();

        when(userRepository.findById(idUser)).thenReturn(Optional.of(userEntity));

        assertThrows(DeleteOwnUserException.class, () -> springDeleteUserUseCase.execute(idUser, idLoggedUser));
        verify(userRepository).findById(idUser);
        verifyNoInteractions(userRegistrationInvitationRepository, cacheGateway);
    }

    @Test
    void shouldThrowDeleteStandardUserExceptionWhenUserIsStandardUser() {
        var idUser = 1L;
        var idLoggedUser = 2L;
        var userEntity = UserEntity.builder().id(idUser).build();

        when(userRepository.findById(idUser)).thenReturn(Optional.of(userEntity));

        assertThrows(DeleteStandardUserException.class, () -> springDeleteUserUseCase.execute(idUser, idLoggedUser));
        verify(userRepository).findById(idUser);
        verifyNoInteractions(userRegistrationInvitationRepository, cacheGateway);
    }

    @Test
    void shouldDeleteUser() {
        var idUser = 2L;
        var idLoggedUser = 1L;
        var userEntity = UserEntity.builder().id(idUser).build();

        when(userRepository.findById(idUser)).thenReturn(Optional.of(userEntity));
        doNothing().when(cacheGateway).delete(UserCacheKeys.USER.getKey().formatted(idUser));
        doNothing().when(cacheGateway).deleteAllFrom(UserCacheKeys.USERS_PATTERN.getKey());

        springDeleteUserUseCase.execute(idUser, idLoggedUser);

        verify(userRepository).findById(idUser);
        verify(userRegistrationInvitationRepository).disableAllRegistrationInvitationsByUserId(idUser, idLoggedUser);
        verify(userRepository).delete(userEntity);
        verify(cacheGateway).delete(UserCacheKeys.USER.getKey().formatted(idUser));
        verify(cacheGateway).deleteAllFrom(UserCacheKeys.USERS_PATTERN.getKey());

        ArgumentCaptor<UserEntity> userEntityCaptor = ArgumentCaptor.forClass(UserEntity.class);
        verify(userRepository).delete(userEntityCaptor.capture());
        assertEquals(idLoggedUser, userEntityCaptor.getValue().getDeletedBy());
    }
}
