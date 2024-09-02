package br.com.devtt.core.application.usecases;

import br.com.devtt.core.abstractions.application.services.ComparatorService;
import br.com.devtt.core.abstractions.infrastructure.adapters.gateway.database.repositories.CompanyRepository;
import br.com.devtt.core.application.exceptions.CompanyNotFoundException;
import br.com.devtt.infrastructure.adapters.gateway.database.entities.CompanyEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

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
    private final String cnpj = "12345678901234";
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

        springUpdateCompanyUseCase.update(id, name, cnpj, idLoggedUser);

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

        springUpdateCompanyUseCase.update(id, name, cnpj, idLoggedUser);

        verify(companyRepository).findById(id);
        verify(comparatorService).hasChanges(name, companyEntity.getName());
        verify(comparatorService).hasChanges(cnpj, companyEntity.getCnpj());
        verifyNoMoreInteractions(companyRepository);
    }

    @Test
    void shouldThrowCompanyNotFoundExceptionWhenCompanyDoesNotExist() {
        when(companyRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(CompanyNotFoundException.class,
                () -> springUpdateCompanyUseCase.update(id, name, cnpj, idLoggedUser));

        verifyNoMoreInteractions(comparatorService);
        verifyNoMoreInteractions(companyRepository);
    }
}