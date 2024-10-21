package br.com.devtt.core.user.application.usecases;

import br.com.devtt.core.user.abstractions.infrastructure.adapters.gateway.UserRepository;
import br.com.devtt.core.user.application.exceptions.UserNotFoundException;
import br.com.devtt.core.user.application.mappers.UserMapper;
import br.com.devtt.core.user.domain.entities.User;
import br.com.devtt.core.user.infrastructure.adapters.dto.responses.GetUserOutputDto;
import br.com.devtt.core.user.infrastructure.adapters.gateway.database.entities.UserEntity;
import br.com.devtt.core.user.infrastructure.adapters.mappers.GetUserOutputDtoMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class SpringGetUserUseCaseUnitTest {
    @InjectMocks private SpringGetUserUseCase springGetUserUseCase;
    @Mock private UserRepository<UserEntity> userRepository;
    @Mock private UserMapper userMapper;
    @Mock private GetUserOutputDtoMapper adapterMapper;

    @Test
    void shouldThrowUserNotFoundExceptionWhenUserNotFound() {
        var idUser = 1L;

        when(userRepository.findById(idUser)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> springGetUserUseCase.execute(idUser));

        verify(userRepository).findById(idUser);
        verifyNoInteractions(userMapper, adapterMapper);
    }

    @Test
    void shouldReturnUserOutputDtoWhenUserFound() {
        var idUser = 1L;
        var userEntity = UserEntity.builder().id(idUser).build();
        var user = User.builder().id(idUser).build();
        var userOutputDto = new GetUserOutputDto(
                idUser,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                false
        );

        when(userRepository.findById(idUser)).thenReturn(Optional.of(userEntity));
        when(userMapper.toDomain(userEntity)).thenReturn(user);
        when(adapterMapper.toDto(user)).thenReturn(userOutputDto);

        var result = springGetUserUseCase.execute(idUser);

        assertEquals(userOutputDto, result);

        verify(userRepository).findById(idUser);
        verify(userMapper).toDomain(userEntity);
        verify(adapterMapper).toDto(user);
    }
}