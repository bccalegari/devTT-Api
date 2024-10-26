package br.com.devtt.core.user.application.usecases;

import br.com.devtt.core.company.infrastructure.adapters.gateway.database.entities.CompanyEntity;
import br.com.devtt.core.user.abstractions.infrastructure.adapters.gateway.UserRepository;
import br.com.devtt.core.user.application.exceptions.UserAlreadyExistsException;
import br.com.devtt.core.user.application.exceptions.UserNotFoundException;
import br.com.devtt.core.user.infrastructure.adapters.dto.UpdateUserUseCaseValidatorDto;
import br.com.devtt.core.user.infrastructure.adapters.dto.requests.UpdateUserInputDto;
import br.com.devtt.core.user.infrastructure.adapters.gateway.database.entities.UserEntity;
import br.com.devtt.enterprise.abstractions.application.services.ComparatorService;
import br.com.devtt.enterprise.abstractions.application.services.ValidatorService;
import br.com.devtt.enterprise.application.exceptions.InsufficientCredentialsException;
import br.com.devtt.enterprise.infrastructure.adapters.gateway.database.entities.CityEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class SpringUpdateUserUseCaseUnitTest {
    @InjectMocks
    private SpringUpdateUserUseCase springUpdateUserUseCase;
    @Mock
    private UserRepository<UserEntity> userRepository;
    @Mock
    private ValidatorService<UpdateUserUseCaseValidatorDto> validatorService;
    @Mock
    private ComparatorService comparatorService;

    @Test
    void shouldThrowUserNotFoundExceptionWhenUserIsNotFound() {
        var userToBeUpdatedId = 1L;
        var idLoggedUser = 1L;
        var loggedUserRole = "Manager";
        var loggedUserCompanyId = 1;

        when(userRepository.findById(userToBeUpdatedId)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> {
            springUpdateUserUseCase.execute(null, userToBeUpdatedId, idLoggedUser, loggedUserRole, loggedUserCompanyId);
        });

        verify(userRepository).findById(userToBeUpdatedId);
        verifyNoInteractions(validatorService, comparatorService);
    }

    @Test
    void shouldThrowInsufficientCredentialsExceptionWhenValidatorServiceReturnsFalse() {
        var inputDto = UpdateUserInputDto.builder().build();

        var userToBeUpdatedId = 2L;
        var userToBeUpdatedCompanyId = 2;
        var idLoggedUser = 1L;
        var loggedUserRole = "Manager";
        var loggedUserCompanyId = 1;

        var validatorDto = UpdateUserUseCaseValidatorDto.builder()
                .searchedUserId(userToBeUpdatedId)
                .searchedUserCompanyId(userToBeUpdatedCompanyId)
                .loggedUserId(idLoggedUser)
                .loggedUserRole(loggedUserRole)
                .loggedUserCompanyId(loggedUserCompanyId)
                .build();

        var userEntity = UserEntity.builder().id(userToBeUpdatedId).company(CompanyEntity.builder().id(userToBeUpdatedCompanyId).build()).build();

        when(userRepository.findById(userToBeUpdatedId)).thenReturn(Optional.of(userEntity));
        when(validatorService.execute(validatorDto)).thenReturn(false);

        assertThrows(InsufficientCredentialsException.class, () -> {
            springUpdateUserUseCase.execute(inputDto, userToBeUpdatedId, idLoggedUser, loggedUserRole, loggedUserCompanyId);
        });

        verify(userRepository).findById(userToBeUpdatedId);
        verify(validatorService).execute(validatorDto);
        verifyNoInteractions(comparatorService);
    }

    @Test
    void shouldNotUpdateUserWhenThereAreNoChanges() {
        var inputDto = UpdateUserInputDto.builder().name("John").build();

        var userToBeUpdatedId = 2L;
        var userToBeUpdatedCompanyId = 2;
        var idLoggedUser = 1L;
        var loggedUserRole = "Manager";
        var loggedUserCompanyId = 1;

        var validatorDto = UpdateUserUseCaseValidatorDto.builder()
                .searchedUserId(userToBeUpdatedId)
                .searchedUserCompanyId(userToBeUpdatedCompanyId)
                .loggedUserId(idLoggedUser)
                .loggedUserRole(loggedUserRole)
                .loggedUserCompanyId(loggedUserCompanyId)
                .build();

        var userEntity = UserEntity.builder()
                .id(userToBeUpdatedId)
                .name("John")
                .company(CompanyEntity.builder()
                        .id(userToBeUpdatedCompanyId)
                        .build()
                )
                .city(CityEntity.builder().build())
                .build();

        when(userRepository.findById(userToBeUpdatedId)).thenReturn(Optional.of(userEntity));
        when(validatorService.execute(validatorDto)).thenReturn(true);
        when(comparatorService.hasChanges(inputDto.getName(), userEntity.getName())).thenReturn(false);

        springUpdateUserUseCase.execute(inputDto, userToBeUpdatedId, idLoggedUser, loggedUserRole, loggedUserCompanyId);

        verify(userRepository).findById(userToBeUpdatedId);
        verify(validatorService).execute(validatorDto);
        verify(comparatorService).hasChanges(inputDto.getName(), userEntity.getName());
        verifyNoMoreInteractions(userRepository, validatorService);
    }

    @Test
    void shouldThrowUserAlreadyExistsExceptionWhenUserWithSamePhoneAlreadyExists() {
        var inputDto = UpdateUserInputDto.builder().phone("123456789").build();

        var userToBeUpdatedId = 2L;
        var userToBeUpdatedCompanyId = 2;
        var idLoggedUser = 1L;
        var loggedUserRole = "Manager";
        var loggedUserCompanyId = 1;

        var validatorDto = UpdateUserUseCaseValidatorDto.builder()
                .searchedUserId(userToBeUpdatedId)
                .searchedUserCompanyId(userToBeUpdatedCompanyId)
                .loggedUserId(idLoggedUser)
                .loggedUserRole(loggedUserRole)
                .loggedUserCompanyId(loggedUserCompanyId)
                .build();

        var userEntity = UserEntity.builder()
                .id(userToBeUpdatedId)
                .phone(123456789L)
                .company(CompanyEntity.builder()
                        .id(userToBeUpdatedCompanyId)
                        .build()
                )
                .city(CityEntity.builder().build())
                .build();

        var userEntityWithSamePhone = Optional.of(UserEntity.builder().id(3L).phone(123456789L).build());

        when(userRepository.findById(userToBeUpdatedId)).thenReturn(Optional.of(userEntity));
        when(validatorService.execute(validatorDto)).thenReturn(true);
        when(comparatorService.hasChanges(any(), any())).thenReturn(true);
        when(userRepository.findByPhone(Long.valueOf(inputDto.getPhone()))).thenReturn(userEntityWithSamePhone);

        assertThrows(UserAlreadyExistsException.class, () -> {
            springUpdateUserUseCase.execute(inputDto, userToBeUpdatedId, idLoggedUser, loggedUserRole, loggedUserCompanyId);
        });

        verify(userRepository).findById(userToBeUpdatedId);
        verify(validatorService).execute(validatorDto);
        verify(comparatorService, times(3)).hasChanges(any(), any());
        verify(userRepository).findByPhone(Long.valueOf(inputDto.getPhone()));
        verifyNoMoreInteractions(validatorService, comparatorService);
    }

    @Test
    void shouldThrowUserAlreadyExistsExceptionWhenUserWithSameEmailAlreadyExists() {
        var inputDto = UpdateUserInputDto.builder().email("test@example.com").build();

        var userToBeUpdatedId = 2L;
        var userToBeUpdatedCompanyId = 2;
        var idLoggedUser = 1L;
        var loggedUserRole = "Manager";
        var loggedUserCompanyId = 1;

        var validatorDto = UpdateUserUseCaseValidatorDto.builder()
                .searchedUserId(userToBeUpdatedId)
                .searchedUserCompanyId(userToBeUpdatedCompanyId)
                .loggedUserId(idLoggedUser)
                .loggedUserRole(loggedUserRole)
                .loggedUserCompanyId(loggedUserCompanyId)
                .build();

        var userEntity = UserEntity.builder()
                .id(userToBeUpdatedId)
                .email("old@example.com")
                .company(CompanyEntity.builder()
                        .id(userToBeUpdatedCompanyId)
                        .build()
                )
                .city(CityEntity.builder().build())
                .build();

        var userEntityWithSameEmail = Optional.of(UserEntity.builder().id(3L).email("test@example.com").build());

        when(userRepository.findById(userToBeUpdatedId)).thenReturn(Optional.of(userEntity));
        when(validatorService.execute(validatorDto)).thenReturn(true);
        when(comparatorService.hasChanges(any(), any())).thenReturn(true);
        when(userRepository.findByEmail(inputDto.getEmail())).thenReturn(userEntityWithSameEmail);

        assertThrows(UserAlreadyExistsException.class, () -> {
            springUpdateUserUseCase.execute(inputDto, userToBeUpdatedId, idLoggedUser, loggedUserRole, loggedUserCompanyId);
        });

        verify(userRepository).findById(userToBeUpdatedId);
        verify(validatorService).execute(validatorDto);
        verify(comparatorService, times(4)).hasChanges(any(), any());
        verify(userRepository).findByEmail(inputDto.getEmail());
        verifyNoMoreInteractions(validatorService, comparatorService);
    }

    @Test
    void shouldThrowUserAlreadyExistsExceptionWhenUserWithSameCpfAlreadyExists() {
        var inputDto = UpdateUserInputDto.builder().cpf("12345678901").build();

        var userToBeUpdatedId = 2L;
        var userToBeUpdatedCompanyId = 2;
        var idLoggedUser = 1L;
        var loggedUserRole = "Manager";
        var loggedUserCompanyId = 1;

        var validatorDto = UpdateUserUseCaseValidatorDto.builder()
                .searchedUserId(userToBeUpdatedId)
                .searchedUserCompanyId(userToBeUpdatedCompanyId)
                .loggedUserId(idLoggedUser)
                .loggedUserRole(loggedUserRole)
                .loggedUserCompanyId(loggedUserCompanyId)
                .build();

        var userEntity = UserEntity.builder()
                .id(userToBeUpdatedId)
                .cpf("12345678901")
                .company(CompanyEntity.builder()
                        .id(userToBeUpdatedCompanyId)
                        .build()
                )
                .city(CityEntity.builder().build())
                .build();

        var userEntityWithSameCpf = Optional.of(UserEntity.builder().id(3L).cpf("12345678901").build());

        when(userRepository.findById(userToBeUpdatedId)).thenReturn(Optional.of(userEntity));
        when(validatorService.execute(validatorDto)).thenReturn(true);
        when(comparatorService.hasChanges(any(), any())).thenReturn(true);
        when(userRepository.findByCpf(inputDto.getCpf())).thenReturn(userEntityWithSameCpf);

        assertThrows(UserAlreadyExistsException.class, () -> {
            springUpdateUserUseCase.execute(inputDto, userToBeUpdatedId, idLoggedUser, loggedUserRole, loggedUserCompanyId);
        });

        verify(userRepository).findById(userToBeUpdatedId);
        verify(validatorService).execute(validatorDto);
        verify(comparatorService, times(6)).hasChanges(any(), any());
        verify(userRepository).findByCpf(inputDto.getCpf());
        verifyNoMoreInteractions(validatorService, comparatorService);
    }

    @Test
    void shouldUpdateUserWhenThereAreChanges() {
        var inputDto = UpdateUserInputDto.builder().name("New John").build();

        var userToBeUpdatedId = 2L;
        var userToBeUpdatedCompanyId = 2;
        var idLoggedUser = 1L;
        var loggedUserRole = "Manager";
        var loggedUserCompanyId = 1;

        var validatorDto = UpdateUserUseCaseValidatorDto.builder()
                .searchedUserId(userToBeUpdatedId)
                .searchedUserCompanyId(userToBeUpdatedCompanyId)
                .loggedUserId(idLoggedUser)
                .loggedUserRole(loggedUserRole)
                .loggedUserCompanyId(loggedUserCompanyId)
                .build();

        var userEntity = UserEntity.builder()
                .id(userToBeUpdatedId)
                .name("John")
                .company(CompanyEntity.builder()
                        .id(userToBeUpdatedCompanyId)
                        .build()
                )
                .city(CityEntity.builder().build())
                .build();

        when(userRepository.findById(userToBeUpdatedId)).thenReturn(Optional.of(userEntity));
        when(userRepository.findByPhone(null)).thenReturn(Optional.empty());
        when(userRepository.findByEmail(null)).thenReturn(Optional.empty());
        when(userRepository.findByCpf(null)).thenReturn(Optional.empty());
        when(validatorService.execute(validatorDto)).thenReturn(true);
        when(comparatorService.hasChanges(any(), any())).thenReturn(true);

        springUpdateUserUseCase.execute(inputDto, userToBeUpdatedId, idLoggedUser, loggedUserRole, loggedUserCompanyId);

        verify(userRepository).findById(userToBeUpdatedId);
        verify(validatorService).execute(validatorDto);
        verify(comparatorService, times(14)).hasChanges(any(), any());
        verify(userRepository).findByPhone(null);
        verify(userRepository).findByEmail(null);
        verify(userRepository).findByCpf(null);
        verify(userRepository).update(userEntity);
        verifyNoMoreInteractions(userRepository, validatorService, comparatorService);
    }
}
