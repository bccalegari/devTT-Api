package br.com.devtt.core.application.usecases;

import br.com.devtt.core.abstractions.infrastructure.adapters.gateway.database.repositories.CompanyRepository;
import br.com.devtt.core.application.exceptions.CompanyAlreadyExistsException;
import br.com.devtt.core.application.mappers.CompanyMapper;
import br.com.devtt.core.domain.entities.Company;
import br.com.devtt.core.domain.valueobjects.Auditing;
import br.com.devtt.core.domain.valueobjects.Cnpj;
import br.com.devtt.infrastructure.adapters.gateway.database.entities.CompanyEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class SpringCreateCompanyUseCaseUnitTest {
    @InjectMocks private SpringCreateCompanyUseCase springCreateCompanyUseCase;
    @Mock private CompanyRepository<CompanyEntity> companyRepository;
    @Mock private CompanyMapper companyMapper;
    private Company companyDomain;
    private CompanyEntity companyEntity;

    @BeforeEach
    void setUp() {
        companyDomain = Company.builder()
                .name("Company")
                .cnpj(new Cnpj("12345678901234"))
                .auditing(Auditing.builder().createdBy(1L).build())
                .build();
        companyEntity = CompanyEntity.builder()
                .name(companyDomain.getName())
                .cnpj(companyDomain.getCnpj().getValue())
                .createdBy(companyDomain.getAuditing().getCreatedBy())
                .build();
    }

    @Test
    void shouldCreateCompany() {
        when(companyRepository.findByCnpj(companyDomain.getCnpj().getValue())).thenReturn(Optional.empty());
        doReturn(companyEntity).when(companyMapper).toEntity(any(Company.class));
        when(companyRepository.save(companyEntity)).thenReturn(companyEntity);

        springCreateCompanyUseCase.create(
                companyDomain.getName(), companyDomain.getCnpj().getValue(), companyDomain.getAuditing().getCreatedBy()
        );

        verify(companyRepository).findByCnpj(companyDomain.getCnpj().getValue());
        verify(companyMapper).toEntity(any(Company.class));
        verify(companyRepository).save(companyEntity);
    }

    @Test
    void shouldThrowCompanyAlreadyExistsException() {
        when(companyRepository.findByCnpj(companyDomain.getCnpj().getValue())).thenReturn(Optional.of(companyEntity));

        assertThrows(CompanyAlreadyExistsException.class, () -> {
            springCreateCompanyUseCase.create(
                    companyDomain.getName(), companyDomain.getCnpj().getValue(),
                    companyDomain.getAuditing().getCreatedBy()
            );
        });

        verify(companyRepository).findByCnpj(companyDomain.getCnpj().getValue());
        verifyNoMoreInteractions(companyRepository);
        verifyNoInteractions(companyMapper);
    }
}