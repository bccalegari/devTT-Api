package br.com.devtt.core.user.application.services;

import br.com.devtt.core.company.abstractions.infrastructure.adapters.gateway.CompanyRepository;
import br.com.devtt.core.company.application.exceptions.CompanyNotFoundException;
import br.com.devtt.core.company.infrastructure.adapters.gateway.database.entities.CompanyEntity;
import br.com.devtt.core.user.abstractions.infrastructure.adapters.gateway.UserRepository;
import br.com.devtt.core.user.application.exceptions.DeleteOwnUserException;
import br.com.devtt.core.user.application.exceptions.DeleteStandardUserException;
import br.com.devtt.core.user.infrastructure.adapters.gateway.cache.UserCacheKeys;
import br.com.devtt.core.user.infrastructure.adapters.gateway.database.entities.UserEntity;
import br.com.devtt.core.user.invitation.abstractions.infrastructure.adapters.gateway.UserRegistrationInvitationRepository;
import br.com.devtt.core.user.invitation.infrastructure.adapters.gateway.database.entities.UserRegistrationInvitationEntity;
import br.com.devtt.enterprise.abstractions.infrastructure.adapters.gateway.CacheGateway;
import br.com.devtt.enterprise.application.exceptions.CoreException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class SpringDeleteAllCompanyUsersServiceUnitTest {
    @InjectMocks private SpringDeleteAllCompanyUsersService springDeleteAllCompanyUsersService;
    @Mock private CompanyRepository<CompanyEntity> companyRepository;
    @Mock private UserRepository<UserEntity> userRepository;
    @Mock private UserRegistrationInvitationRepository<UserRegistrationInvitationEntity> userRegistrationInvitationRepository;
    @Mock private CacheGateway cacheGateway;

    @Test
    void shouldThrowCompanyNotFoundExceptionWhenCompanyNotFound() {
        var idCompany = 1;
        var idLoggedUser = 1L;

        when(companyRepository.findById(idCompany)).thenReturn(Optional.empty());

        assertThrows(CompanyNotFoundException.class, () -> springDeleteAllCompanyUsersService.execute(idCompany, idLoggedUser));

        verify(companyRepository).findById(idCompany);
        verifyNoInteractions(userRepository, userRegistrationInvitationRepository, cacheGateway);
    }

    @Test
    void shouldThrowDeleteStandardUserExceptionWhenCompanyIsDefault() {
        var idCompany = 1;
        var idLoggedUser = 1L;
        var companyEntity = CompanyEntity.builder().id(idCompany).build();

        when(companyRepository.findById(idCompany)).thenReturn(Optional.of(companyEntity));

        assertThrows(DeleteStandardUserException.class, () -> springDeleteAllCompanyUsersService.execute(idCompany, idLoggedUser));

        verify(companyRepository).findById(idCompany);
        verifyNoInteractions(userRepository, userRegistrationInvitationRepository, cacheGateway);
    }

    @Test
    void shouldThrowCoreExceptionWhenLoggedUserNotFound() {
        var idCompany = 2;
        var idLoggedUser = 1L;
        var companyEntity = CompanyEntity.builder().id(idCompany).build();

        when(companyRepository.findById(idCompany)).thenReturn(Optional.of(companyEntity));
        when(userRepository.findById(idLoggedUser)).thenReturn(Optional.empty());

        assertThrows(CoreException.class, () -> springDeleteAllCompanyUsersService.execute(idCompany, idLoggedUser));

        verify(companyRepository).findById(idCompany);
        verify(userRepository).findById(idLoggedUser);
        verifyNoInteractions(userRegistrationInvitationRepository, cacheGateway);
    }

    @Test
    void shouldThrowDeleteOwnUserExceptionWhenLoggedUserIsFromCompany() {
        var idCompany = 2;
        var idLoggedUser = 1L;
        var companyEntity = CompanyEntity.builder().id(idCompany).build();
        var loggedUserEntity = UserEntity.builder().id(idLoggedUser).company(companyEntity).build();

        when(companyRepository.findById(idCompany)).thenReturn(Optional.of(companyEntity));
        when(userRepository.findById(idLoggedUser)).thenReturn(Optional.of(loggedUserEntity));

        assertThrows(DeleteOwnUserException.class, () -> springDeleteAllCompanyUsersService.execute(idCompany, idLoggedUser));

        verify(companyRepository).findById(idCompany);
        verify(userRepository).findById(idLoggedUser);
        verifyNoInteractions(userRegistrationInvitationRepository, cacheGateway);
    }

    @Test
    void shouldDeleteAllCompanyUsers() {
        var idCompany = 2;
        var idLoggedUser = 1L;
        var companyEntity = CompanyEntity.builder().id(idCompany).build();
        var loggedUserCompanyEntity = CompanyEntity.builder().id(3).build();
        var loggedUserEntity = UserEntity.builder().id(idLoggedUser).company(loggedUserCompanyEntity).build();

        when(companyRepository.findById(idCompany)).thenReturn(Optional.of(companyEntity));
        when(userRepository.findById(idLoggedUser)).thenReturn(Optional.of(loggedUserEntity));
        doNothing().when(cacheGateway).deleteAllFrom(anyString());

        springDeleteAllCompanyUsersService.execute(idCompany, idLoggedUser);

        verify(companyRepository).findById(idCompany);
        verify(userRepository).findById(idLoggedUser);
        verify(userRegistrationInvitationRepository).disableAllRegistrationInvitationsByCompanyId(idCompany, idLoggedUser);
        verify(userRepository).deleteByCompanyId(idCompany, idLoggedUser);
        verify(cacheGateway).deleteAllFrom(UserCacheKeys.USER_PATTERN.getKey());
        verify(cacheGateway).deleteAllFrom(UserCacheKeys.USERS_PATTERN.getKey());
    }
}