package br.com.devtt.core.auth.application.usecases;

import br.com.devtt.core.auth.abstractions.application.services.PasswordEncoderService;
import br.com.devtt.core.auth.abstractions.application.services.TokenService;
import br.com.devtt.core.auth.abstractions.domain.valueobjects.Token;
import br.com.devtt.core.auth.application.exceptions.InvalidPasswordException;
import br.com.devtt.core.auth.domain.valueobjects.JwtToken;
import br.com.devtt.core.company.domain.entities.Company;
import br.com.devtt.core.company.infrastructure.adapters.gateway.database.entities.CompanyEntity;
import br.com.devtt.core.user.abstractions.infrastructure.adapters.gateway.UserRepository;
import br.com.devtt.core.user.application.exceptions.UserNotFoundException;
import br.com.devtt.core.user.application.mappers.UserMapper;
import br.com.devtt.core.user.domain.entities.User;
import br.com.devtt.core.user.infrastructure.adapters.gateway.database.entities.UserEntity;
import br.com.devtt.enterprise.domain.entities.Role;
import br.com.devtt.enterprise.infrastructure.adapters.gateway.database.entities.RoleEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SpringUserLoginUseCaseUnitTest {
    @InjectMocks
    SpringUserLoginUseCase useCase;
    @Mock private TokenService tokenService;
    @Mock private PasswordEncoderService passwordEncoderService;
    @Mock private UserRepository<UserEntity> userRepository;
    @Mock private UserMapper mapper;
    @Captor private ArgumentCaptor<Long> userIdCaptor;
    UserEntity userEntity;
    User user;

    @BeforeEach
    public void setUp() {
        userEntity = UserEntity.builder()
                .id(1L)
                .name("name")
                .lastName("lastName")
                .email("email")
                .password("encodedPassword")
                .role(
                        RoleEntity.builder()
                                .name("role")
                                .build()
                )
                .company(
                        CompanyEntity.builder()
                                .id(1)
                                .build()
                )
                .build();
        user = User.builder()
                .id(1L)
                .name("name")
                .lastName("lastName")
                .email("email")
                .password("encodedPassword")
                .role(
                        Role.builder()
                                .name("role")
                                .build()
                )
                .company(
                        Company.builder()
                                .id(1)
                                .build()
                )
                .build();
    }

    @Test
    void shouldReturnTokenWhenLoginIsSuccessful() {
        when(userRepository.findByEmail("email")).thenReturn(Optional.of(userEntity));
        when(passwordEncoderService.matches("password", "encodedPassword")).thenReturn(true);
        when(mapper.toDomain(userEntity)).thenReturn(user);
        when(tokenService.create(anyLong(), anyString(), anyString(), anyInt())).thenReturn(new JwtToken("token"));

        Token token = useCase.execute("email", "password");

        verify(userRepository).findByEmail("email");
        verify(passwordEncoderService).matches("password", "encodedPassword");
        verify(mapper).toDomain(userEntity);
        verify(tokenService).create(userIdCaptor.capture(), eq("name lastName"), eq("role"), eq(1));

        verifyNoMoreInteractions(userRepository, mapper, passwordEncoderService, tokenService);

        assertNotNull(token);
        assertEquals("token", token.getValue());
        assertEquals(1L, userIdCaptor.getValue());
    }

    @Test
    void shouldThrowUserNotFoundExceptionWhenUserIsNotFound() {
        when(userRepository.findByEmail("email")).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> useCase.execute("email", "password"));

        verify(userRepository).findByEmail("email");
        verifyNoMoreInteractions(userRepository);
    }

    @Test
    void shouldThrowInvalidPasswordExceptionWhenPasswordIsInvalid() {
        when(userRepository.findByEmail("email")).thenReturn(Optional.of(userEntity));
        when(mapper.toDomain(userEntity)).thenReturn(user);
        when(passwordEncoderService.matches("password", "encodedPassword"))
                .thenReturn(false);

        assertThrows(InvalidPasswordException.class, () -> useCase.execute("email", "password"));

        verify(userRepository).findByEmail("email");
        verify(mapper).toDomain(userEntity);
        verify(passwordEncoderService).matches("password", "encodedPassword");
        verifyNoMoreInteractions(userRepository, mapper, passwordEncoderService);
    }
}