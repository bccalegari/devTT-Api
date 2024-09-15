package br.com.devtt.core.company.application.usecases;

import br.com.devtt.core.company.application.exceptions.CompanyAlreadyExistsException;
import br.com.devtt.enterprise.abstractions.application.services.ComparatorService;
import br.com.devtt.core.company.abstractions.infrastructure.adapters.gateway.CompanyRepository;
import br.com.devtt.core.company.application.exceptions.CompanyNotFoundException;
import br.com.devtt.core.company.infrastructure.adapters.gateway.database.entities.CompanyEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class SpringUpdateCompanyUseCaseUnitTest {
    @InjectMocks private SpringUpdateCompanyUseCase springUpdateCompanyUseCase;
    @Mock private CompanyRepository<CompanyEntity> companyRepository;
    @Mock private ComparatorService comparatorService;
    private final Integer id = 1;
    private String name = "Company";
    private String cnpj = "12345678901234";
    private final Long idLoggedUser = 1L;

    @Test
    void shouldUpdateCompanyWhenFieldsAreDifferent() {
        name = "Company Updated";

        var companyEntity = CompanyEntity.builder()
                .id(id)
                .name("Company")
                .cnpj(cnpj)
                .build();

        when(companyRepository.findById(id)).thenReturn(Optional.of(companyEntity));
        when(comparatorService.hasChanges(name, companyEntity.getName())).thenReturn(true);
        when(comparatorService.hasChanges(cnpj, companyEntity.getCnpj())).thenReturn(false);
        doNothing().when(companyRepository).update(companyEntity);

        springUpdateCompanyUseCase.execute(id, name, cnpj, idLoggedUser);

        companyEntity.setUpdatedBy(idLoggedUser);

        verify(companyRepository).findById(id);
        verify(comparatorService).hasChanges(name, "Company");
        verify(comparatorService).hasChanges(cnpj, companyEntity.getCnpj());
        verify(companyRepository).update(companyEntity);
    }

    @Test
    void shouldNotUpdateCompanyWhenFieldsAreEqual() {
        var companyEntity = CompanyEntity.builder()
                .id(id)
                .name(name)
                .cnpj(cnpj)
                .build();

        when(companyRepository.findById(id)).thenReturn(Optional.of(companyEntity));
        when(comparatorService.hasChanges(name, companyEntity.getName())).thenReturn(false);
        when(comparatorService.hasChanges(cnpj, companyEntity.getCnpj())).thenReturn(false);

        springUpdateCompanyUseCase.execute(id, name, cnpj, idLoggedUser);

        verify(companyRepository).findById(id);
        verify(comparatorService).hasChanges(name, companyEntity.getName());
        verify(comparatorService).hasChanges(cnpj, companyEntity.getCnpj());
        verifyNoMoreInteractions(companyRepository);
    }

    @Test
    void shouldThrowCompanyNotFoundExceptionWhenCompanyDoesNotExist() {
        when(companyRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(CompanyNotFoundException.class,
                () -> springUpdateCompanyUseCase.execute(id, name, cnpj, idLoggedUser));

        verifyNoMoreInteractions(comparatorService);
        verifyNoMoreInteractions(companyRepository);
    }

    @Test
    void shouldThrowCompanyExistsExceptionWhenCnpjAlreadyExists() {
        cnpj = "32132123123123";

        var companyEntity = CompanyEntity.builder()
                .id(id)
                .name("Company")
                .cnpj("12345678901234")
                .build();

        when(companyRepository.findById(id)).thenReturn(Optional.of(companyEntity));
        when(comparatorService.hasChanges(name, companyEntity.getName())).thenReturn(false);
        when(comparatorService.hasChanges(cnpj, companyEntity.getCnpj())).thenReturn(true);
        when(companyRepository.findByCnpj(cnpj)).thenReturn(Optional.of(companyEntity));

        assertThrows(CompanyAlreadyExistsException.class,
                () -> springUpdateCompanyUseCase.execute(id, name, cnpj, idLoggedUser));

        verifyNoMoreInteractions(comparatorService);
        verifyNoMoreInteractions(companyRepository);
    }
}