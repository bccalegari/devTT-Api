package br.com.devtt.core.user.application.usecases;

import br.com.devtt.core.user.abstractions.infrastructure.adapters.gateway.UserRepository;
import br.com.devtt.core.user.application.mappers.UserMapper;
import br.com.devtt.core.user.domain.entities.User;
import br.com.devtt.core.user.infrastructure.adapters.dto.responses.GetUserOutputDto;
import br.com.devtt.core.user.infrastructure.adapters.gateway.database.entities.UserEntity;
import br.com.devtt.core.user.infrastructure.adapters.mappers.GetUserOutputDtoMapper;
import br.com.devtt.enterprise.infrastructure.adapters.gateway.database.PageImpl;
import br.com.devtt.enterprise.infrastructure.adapters.gateway.database.PaginationParams;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class SpringGetAllUsersUseCaseUnitTest {
    @InjectMocks private SpringGetAllUsersUseCase springGetAllUsersUseCase;
    @Mock private UserRepository<UserEntity> userRepository;
    @Mock private UserMapper userMapper;
    @Mock private GetUserOutputDtoMapper adapterMapper;

    @Test
    void shouldReturnAnEmptyListWhenThereAreNoUsers() {
        var paginationParams = new PaginationParams(0, 0);
        when(userRepository.findAll(paginationParams)).thenReturn(
                new PageImpl<>(0, 0, 0, 0, 0, List.of())
        );

        var result = springGetAllUsersUseCase.execute(0, 0);

        assertEquals(0, result.users().size());
        verify(userRepository).findAll(any(PaginationParams.class));
        verifyNoInteractions(userMapper, adapterMapper);
    }

    @Test
    void shouldReturnAListOfUsers() {
        var paginationParams = new PaginationParams(0, 0);
        var userEntity = UserEntity.builder().id(1L).build();
        var userEntityList = List.of(userEntity);
        var userDomain = User.builder().id(1L).build();
        var userDto = new GetUserOutputDto(
                1L, null, null, null, null, null, null, null,
                null, null, null, false
        );

        when(userRepository.findAll(paginationParams)).thenReturn(
                new PageImpl<>(0, 0, 1, 1, 1, userEntityList)
        );
        when(userMapper.toDomain(userEntity)).thenReturn(userDomain);
        when(adapterMapper.toDto(userDomain)).thenReturn(userDto);

        var result = springGetAllUsersUseCase.execute(0, 0);

        assertEquals(1, result.users().size());
        assertEquals(1L, result.users().getFirst().id());
        verify(userRepository).findAll(paginationParams);
        verify(userMapper).toDomain(userEntity);
        verify(adapterMapper).toDto(userDomain);
    }
}