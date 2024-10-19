package br.com.devtt.core.company.application.usecases;

import br.com.devtt.core.company.abstractions.infrastructure.adapters.gateway.CompanyRepository;
import br.com.devtt.core.company.application.exceptions.CompanyNotFoundException;
import br.com.devtt.core.company.application.exceptions.DeleteOwnCompanyException;
import br.com.devtt.core.company.application.exceptions.DeleteStandardCompanyException;
import br.com.devtt.core.company.infrastructure.adapters.gateway.database.entities.CompanyEntity;
import br.com.devtt.core.user.abstractions.infrastructure.adapters.gateway.UserRepository;
import br.com.devtt.core.user.infrastructure.adapters.gateway.database.entities.UserEntity;
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
public class SpringDeleteCompanyUseCaseUnitTest {
    @InjectMocks private SpringDeleteCompanyUseCase springDeleteCompanyUseCase;
    @Mock private CompanyRepository<CompanyEntity> companyRepository;
    @Mock private UserRepository<UserEntity> userRepository;

    @Test
    void shouldDeleteCompany() {
        var companyId = 2;
        var loggedUserId = 1L;

        var companyEntity = CompanyEntity.builder().id(companyId).build();
        var userEntity = UserEntity.builder().id(loggedUserId).company(CompanyEntity.builder().id(3).build()).build();

        when(companyRepository.findById(2)).thenReturn(Optional.of(companyEntity));
        when(userRepository.findById(1L)).thenReturn(Optional.of(userEntity));

        springDeleteCompanyUseCase.execute(companyId, loggedUserId);

        verify(companyRepository).findById(companyId);
        verify(userRepository).findById(loggedUserId);
        verify(companyRepository).delete(companyEntity);
    }

    @Test
    void shouldThrowCompanyNotFoundException() {
        var companyId = 2;
        var loggedUserId = 1L;

        when(companyRepository.findById(2)).thenReturn(Optional.empty());

        assertThrows(CompanyNotFoundException.class, () -> springDeleteCompanyUseCase.execute(companyId, loggedUserId));
        verify(companyRepository).findById(companyId);
        verifyNoInteractions(userRepository);
    }

    @Test
    void shouldThrowDeleteOwnCompanyException() {
        var companyId = 2;
        var loggedUserId = 1L;

        var companyEntity = CompanyEntity.builder().id(companyId).build();
        var userEntity = UserEntity.builder().id(loggedUserId).company(companyEntity).build();

        when(companyRepository.findById(2)).thenReturn(Optional.of(companyEntity));
        when(userRepository.findById(1L)).thenReturn(Optional.of(userEntity));

        assertThrows(DeleteOwnCompanyException.class, () -> springDeleteCompanyUseCase.execute(companyId, loggedUserId));
        verify(companyRepository).findById(companyId);
        verify(userRepository).findById(loggedUserId);
    }

    @Test
    void shouldThrowDeleteStandardCompanyException() {
        var companyId = 1;
        var loggedUserId = 1L;

        var companyEntity = CompanyEntity.builder().id(2).build();
        var userEntity = UserEntity.builder().id(loggedUserId).company(companyEntity).build();

        when(companyRepository.findById(companyId)).thenReturn(Optional.of(CompanyEntity.builder().id(1).build()));
        when(userRepository.findById(loggedUserId)).thenReturn(Optional.of(userEntity));

        assertThrows(DeleteStandardCompanyException.class, () -> springDeleteCompanyUseCase.execute(companyId, loggedUserId));
        verify(companyRepository).findById(companyId);
        verify(userRepository).findById(loggedUserId);
    }

    @Test
    void shouldThrowCoreException() {
        var companyId = 2;
        var loggedUserId = 1L;

        var companyEntity = CompanyEntity.builder().id(companyId).build();

        when(companyRepository.findById(2)).thenReturn(Optional.of(companyEntity));
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(CoreException.class, () -> springDeleteCompanyUseCase.execute(companyId, loggedUserId));
        verify(companyRepository).findById(companyId);
        verify(userRepository).findById(loggedUserId);
    }
}
