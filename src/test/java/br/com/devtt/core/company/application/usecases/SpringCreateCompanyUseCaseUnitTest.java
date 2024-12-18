package br.com.devtt.core.company.application.usecases;

import br.com.devtt.core.company.abstractions.infrastructure.adapters.gateway.CompanyRepository;
import br.com.devtt.core.company.application.exceptions.CompanyAlreadyExistsException;
import br.com.devtt.core.company.application.mappers.CompanyMapper;
import br.com.devtt.core.company.domain.entities.Company;
import br.com.devtt.core.company.domain.valueobjects.Cnpj;
import br.com.devtt.core.company.infrastructure.adapters.gateway.cache.CompanyCacheKeys;
import br.com.devtt.core.company.infrastructure.adapters.gateway.database.entities.CompanyEntity;
import br.com.devtt.enterprise.abstractions.infrastructure.adapters.gateway.CacheGateway;
import br.com.devtt.enterprise.domain.valueobjects.Auditing;
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
    @Mock private CacheGateway cacheGateway;
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
        doNothing().when(cacheGateway).deleteAllFrom(CompanyCacheKeys.COMPANIES_PATTERN.getKey());

        springCreateCompanyUseCase.execute(
                companyDomain.getName(), companyDomain.getCnpj().getValue(), companyDomain.getAuditing().getCreatedBy()
        );

        verify(companyRepository).findByCnpj(companyDomain.getCnpj().getValue());
        verify(companyMapper).toEntity(any(Company.class));
        verify(companyRepository).save(companyEntity);
        verify(cacheGateway).deleteAllFrom(CompanyCacheKeys.COMPANIES_PATTERN.getKey());
    }

    @Test
    void shouldThrowCompanyAlreadyExistsException() {
        when(companyRepository.findByCnpj(companyDomain.getCnpj().getValue())).thenReturn(Optional.of(companyEntity));

        assertThrows(CompanyAlreadyExistsException.class, () -> {
            springCreateCompanyUseCase.execute(
                    companyDomain.getName(), companyDomain.getCnpj().getValue(),
                    companyDomain.getAuditing().getCreatedBy()
            );
        });

        verify(companyRepository).findByCnpj(companyDomain.getCnpj().getValue());
        verifyNoMoreInteractions(companyRepository);
        verifyNoInteractions(companyMapper, cacheGateway);
    }
}