package br.com.devtt.core.user.application.usecases;

import br.com.devtt.core.company.domain.entities.Company;
import br.com.devtt.core.company.infrastructure.adapters.gateway.database.entities.CompanyEntity;
import br.com.devtt.core.user.abstractions.infrastructure.adapters.gateway.UserRepository;
import br.com.devtt.core.user.application.exceptions.UserNotFoundException;
import br.com.devtt.core.user.application.mappers.UserMapper;
import br.com.devtt.core.user.domain.entities.User;
import br.com.devtt.core.user.infrastructure.adapters.dto.GetUserUseCaseValidatorDto;
import br.com.devtt.core.user.infrastructure.adapters.dto.responses.GetUserOutputDto;
import br.com.devtt.core.user.infrastructure.adapters.gateway.cache.UserCacheKeys;
import br.com.devtt.core.user.infrastructure.adapters.gateway.database.entities.UserEntity;
import br.com.devtt.core.user.infrastructure.adapters.mappers.GetUserOutputDtoMapper;
import br.com.devtt.enterprise.abstractions.application.services.ValidatorService;
import br.com.devtt.enterprise.abstractions.infrastructure.adapters.gateway.CacheGateway;
import br.com.devtt.enterprise.application.exceptions.InsufficientCredentialsException;
import com.fasterxml.jackson.databind.ObjectMapper;
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
    @Mock private ValidatorService<GetUserUseCaseValidatorDto> validatorService;
    @Mock private CacheGateway cacheGateway;
    @Mock private UserMapper userMapper;
    @Mock private GetUserOutputDtoMapper adapterMapper;
    @Mock private ObjectMapper objectMapper;

    @Test
    void shouldThrowUserNotFoundExceptionWhenUserNotFound() {
        var idUser = 1L;
        var loggedUserId = 2L;
        var loggedUserRole = "MASTER";
        var loggedUserCompanyId = 3;

        when(cacheGateway.get(UserCacheKeys.USER.getKey().formatted(idUser))).thenReturn(null);
        when(userRepository.findById(idUser)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class,
                () -> springGetUserUseCase.execute(idUser, loggedUserId, loggedUserRole, loggedUserCompanyId)
        );

        verify(cacheGateway).get(UserCacheKeys.USER.getKey().formatted(idUser));
        verify(userRepository).findById(idUser);
        verifyNoMoreInteractions(cacheGateway);
        verifyNoInteractions(validatorService, userMapper, adapterMapper);
    }

    @Test
    void shouldReturnUserOutputDtoWhenUserFound() {
        var idUser = 1L;
        var searchedUserCompanyId = 2;
        var loggedUserId = 2L;
        var loggedUserRole = "MASTER";
        var loggedUserCompanyId = 3;

        var validatorDto = GetUserUseCaseValidatorDto.builder()
                .searchedUserId(idUser)
                .searchedUserCompanyId(searchedUserCompanyId)
                .loggedUserId(loggedUserId)
                .loggedUserRole(loggedUserRole)
                .loggedUserCompanyId(loggedUserCompanyId)
                .build();

        var userEntity = UserEntity.builder().id(idUser).company(CompanyEntity.builder().id(searchedUserCompanyId).build()).build();
        var user = User.builder().id(idUser).company(Company.builder().id(searchedUserCompanyId).build()).build();
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

        when(cacheGateway.get(UserCacheKeys.USER.getKey().formatted(idUser))).thenReturn(null);
        when(userRepository.findById(idUser)).thenReturn(Optional.of(userEntity));
        when(validatorService.execute(validatorDto)).thenReturn(true);
        when(userMapper.toDomain(userEntity)).thenReturn(user);
        when(adapterMapper.toDto(user)).thenReturn(userOutputDto);
        doNothing().when(cacheGateway).put(UserCacheKeys.USER.getKey().formatted(idUser), userOutputDto);

        var result = springGetUserUseCase.execute(idUser, loggedUserId, loggedUserRole, loggedUserCompanyId);

        assertEquals(userOutputDto, result);

        verify(cacheGateway).get(UserCacheKeys.USER.getKey().formatted(idUser));
        verify(userRepository).findById(idUser);
        verify(validatorService).execute(validatorDto);
        verify(userMapper).toDomain(userEntity);
        verify(adapterMapper).toDto(user);
        verify(cacheGateway).put(UserCacheKeys.USER.getKey().formatted(idUser), userOutputDto);
        verifyNoMoreInteractions(cacheGateway);
    }

    @Test
    void shouldReturnUserOutputDtoFromCacheWhenUserFound() {
        var idUser = 1L;
        var loggedUserId = 2L;
        var loggedUserRole = "MASTER";
        var loggedUserCompanyId = 3;

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

        when(cacheGateway.get(UserCacheKeys.USER.getKey().formatted(idUser))).thenReturn(userOutputDto);
        when(objectMapper.convertValue(userOutputDto, GetUserOutputDto.class)).thenReturn(userOutputDto);

        var result = springGetUserUseCase.execute(idUser, loggedUserId, loggedUserRole, loggedUserCompanyId);

        assertEquals(userOutputDto, result);

        verify(cacheGateway).get(UserCacheKeys.USER.getKey().formatted(idUser));
        verifyNoMoreInteractions(cacheGateway);
        verifyNoInteractions(userRepository, validatorService, userMapper, adapterMapper);
    }

    @Test
    void shouldThrowInsufficientCredentialsWhenValidatorServiceReturnFalse() {
        var idUser = 1L;
        var searchedUserCompanyId = 2;
        var loggedUserId = 2L;
        var loggedUserRole = "USER";
        var loggedUserCompanyId = 3;

        var validatorDto = GetUserUseCaseValidatorDto.builder()
                .searchedUserId(idUser)
                .searchedUserCompanyId(searchedUserCompanyId)
                .loggedUserId(loggedUserId)
                .loggedUserRole(loggedUserRole)
                .loggedUserCompanyId(loggedUserCompanyId)
                .build();

        var userEntity = UserEntity.builder().id(idUser).company(CompanyEntity.builder().id(searchedUserCompanyId).build()).build();

        when(cacheGateway.get(UserCacheKeys.USER.getKey().formatted(idUser))).thenReturn(null);
        when(userRepository.findById(idUser)).thenReturn(Optional.of(userEntity));
        when(validatorService.execute(validatorDto)).thenReturn(false);

        assertThrows(InsufficientCredentialsException.class,
                () -> springGetUserUseCase.execute(idUser, loggedUserId, loggedUserRole, loggedUserCompanyId)
        );

        verify(cacheGateway).get(UserCacheKeys.USER.getKey().formatted(idUser));
        verify(userRepository).findById(idUser);
        verify(validatorService).execute(validatorDto);
        verifyNoMoreInteractions(cacheGateway);
        verifyNoInteractions(userMapper, adapterMapper);
    }
}